package com.conceptapp.videos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jdo.PersistenceManager;
import com.google.appengine.api.datastore.Key;
import com.conceptapp.util.DB;
import com.conceptapp.util.Histogram;
import com.conceptapp.util.NotFoundException;

public class Search {

	protected PersistenceManager pm;
	protected List<Video> videos;

	public Search(PersistenceManager p) {
		pm = p;
	}

	public List<Video> getVideos(KeywordSet ks) throws NotFoundException {
		search(ks, "", 0);
		return videos;
	}

	public List<Video> getVideos(KeywordSet ks, String order)
	throws NotFoundException {
		search(ks, order, 0);
		return videos;
	}

	public List<Video> getVideos(KeywordSet ks, String order, int limitFrom, int limitTo)
	throws NotFoundException {
		search(ks, order, limitFrom);
		limitTo = Math.min(limitTo, videos.size());
		List<Video> filtered = new ArrayList<Video>();
		for (int i = limitFrom; i < limitTo; i++) {
			filtered.add(videos.get(i));
		}
		if (filtered.size() == 0) {
			throw new NotFoundException();
		}
		return filtered;
	}

	public int getTotal() {
		if (videos != null) return videos.size();
		else return 0;
	}

	protected void search(KeywordSet ks, String order, int limitFrom)
	throws NotFoundException {
		Histogram<String> videoIds = getVideoIds(ks);
		if (limitFrom >= videoIds.size()) {
			throw new NotFoundException();
		}
		query(videoIds);
		if (videos.size() == 0) {
			throw new NotFoundException();
		}
		sort(order);
	}

	protected Histogram<String> getVideoIds(KeywordSet ks) {
		Histogram<String> videoIds = new Histogram<String>();
		// Get tags
		Tag[] tags = getTags(ks);
		if (tags.length > 0) {
			// Get video IDs
			for (Tag tag : tags) {
				videoIds.add(tag.getVideoIds());
			}
			videoIds.filter(tags.length / 2);
			// Find best 100 matches
			videoIds.limit(100);
		}
		return videoIds;
	}

	protected Tag[] getTags(KeywordSet ks) {
		// Find
		Class<Tag> tagClass = Tag.class;
		Set<Tag> found = new HashSet<Tag>();
		int i = 0;
		for (String keyword : ks) {
			Tag tag = DB.getRecord(pm, tagClass, Tag.createKey(keyword));
			if (tag != null) {
				int tagSize = tag.size();
				if (tagSize > 0 && tagSize < 1000) {
					found.add(tag);
				}
			}
			// Process only first 10 keywords
			if (++i > 9) {
				break;
			}
		}
		// Sort by rareness
		Tag[] sorted = found.toArray(new Tag[0]);
		Arrays.sort(sorted, new Comparator<Tag>() {
			public int compare(Tag a, Tag b) {
				return a.size() - b.size();
			}
		});
		return sorted;
	}

	protected void query(Histogram<String> videoIds) {
		videos = new ArrayList<Video>();
		// Get videos
		List<String> ids = new ArrayList<String>(videoIds.getKeys());
		List<Key> keys = new ArrayList<Key>(ids.size());
		for (String id : ids) {
			keys.add(Video.createKey(id));
		}
		List<Video> results = (List<Video>) DB.getRecords(pm, Video.class, keys);
		// Set relevance
		for (Video video : results) {
			try {
				video.setRelevance(videoIds.getCount(video.getId()));
				videos.add(video);
			} catch (NullPointerException e) {
			}
		}
	}

	protected void sort(String order) {
		// Sort
		if (order.equals("p")) {
			// By popularity
			Collections.sort(videos, new Comparator<Video>() {
				public int compare(Video a, Video b) {
					double pa = a.getPopularity();
					double pb = b.getPopularity();
					if (pa < pb) return 1;
					if (pa > pb) return -1;
					return 0;
				}
			});
		} else if (order.equals("d")) {
			// By date
			Collections.sort(videos, new Comparator<Video>() {
				public int compare(Video a, Video b) {
					Date da = a.getAddedDate();
					Date db = b.getAddedDate();
					int cd = da.compareTo(db);
					if (cd != 0) return -cd;
					double pa = a.getPopularity();
					double pb = b.getPopularity();
					if (pa < pb) return 1;
					if (pa > pb) return -1;
					return 0;
				}
			});
		} else if (order.equals("t")) {
			// By title
			Collections.sort(videos, new Comparator<Video>() {
				public int compare(Video a, Video b) {
					String sa = a.getTitle();
					String sb = b.getTitle();
					int cs = sa.compareToIgnoreCase(sb);
					if (cs != 0) return cs;
					double pa = a.getPopularity();
					double pb = b.getPopularity();
					if (pa < pb) return -1;
					if (pa > pb) return 1;
					return 0;
				}
			});
		} else {
			// By relevance
			Collections.sort(videos, new Comparator<Video>() {
				public int compare(Video a, Video b) {
					int ra = a.getRelevance();
					int rb = b.getRelevance();
					if (ra != rb) return ra - rb;
					double pa = a.getPopularity();
					double pb = b.getPopularity();
					if (pa < pb) return 1;
					if (pa > pb) return -1;
					return 0;
				}
			});
		}
	}

}