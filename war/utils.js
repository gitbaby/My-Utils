/**
 * @constructor
 */
MyUtils = function() {};

/**
 * Returns the value of passed radio group or select element.
 *
 * @param {object} oGroup Rario group or select element
 * @return Selected value
 * @type string
 */
MyUtils.getRadioValue = function(oGroup) {
	if (!oGroup) {
		return '';
	}
	if (typeof oGroup.selectedIndex == 'number') {
		// Select box
		return oGroup[oGroup.selectedIndex].value;
	} else {
		// Radio
		var iItems = oGroup.length;
		if (!iItems) {
			return '';
		}
		var oItem;
		for (var iItem = 0; iItem < iItems; iItem++) {
			oItem = oGroup[iItem];
			if (oItem.checked) {
				return oItem.value;
			}
		}
	}
	return '';
};

/**
 * Sets the value of passed radio group or select element.
 *
 * @param {object} oGroup Rario group or select element
 * @param {string} sValue Selected value
 */
MyUtils.setRadioValue = function(oGroup, sValue) {
	if (!oGroup) {
		return;
	}
	var iItems = oGroup.length;
	if (!iItems) {
		return;
	}
	var oItem;
	for (var iItem = 0; iItem < iItems; iItem++) {
		oItem = oGroup[iItem];
		if (oItem.value == sValue) {
			if (typeof oGroup.selectedIndex == 'number') {
				// Select box
				oGroup.selectedIndex = iItem;
			} else {
				// Radio
				oItem.checked = 'checked';
			}
			return;
		}
	}
};

/**
 * Disables or enables radio group or select element.
 *
 * @param {object} oGroup Rario group or select element
 * @param {boolean} bDisabled true - disable; false - enable
 */
MyUtils.setRadioDisabled = function(oGroup, bDisabled) {
	if (!oGroup) {
		return;
	}
	if (typeof oGroup.selectedIndex == 'number') {
		// Select box
		oGroup.disabled = bDisabled;
		return;
	}
	var iItems = oGroup.length;
	if (!iItems) {
		return;
	}
	// Radio
	for (var iItem = 0; iItem < iItems; iItem++) {
		oGroup[iItem].disabled = bDisabled;
	}
};

/**
 * /\s+/g.
 * @final
 */
MyUtils.regexpSpacePlus = /\s+/g;

/**
 * /^\s/.
 * @private
 * @final
 */
MyUtils.regexpSpaceLeft = /^\s+/;

/**
 * /\s$/.
 * @private
 * @final
 */
MyUtils.regexpSpaceRight = /\s+$/;

/**
 * Removes leading (/^\s+/) and trailing (/\s+$/) white spaces.
 *
 * @param {string} s Input string
 * @return Trimmed string
 * @type string
 */
MyUtils.trim = function(s) {
	if (typeof s == "string") {
		s = s.replace(MyUtils.regexpSpaceLeft, "");
		s = s.replace(MyUtils.regexpSpaceRight, "");
	} else {
		s = "";
	}
	return s;
};

/**
 * Replaces multiple consecutive white spaces (/\s+/g) with single space
 * character. Removes leading and trailing white spaces.
 *
 * @param {string} s Input string
 * @return Trimmed string
 * @type string
 */
MyUtils.multispacekill = function(s) {
	if (typeof s == "string") {
		s = MyUtils.trim(s);
		s = s.replace(MyUtils.regexpSpacePlus, " ");
	} else {
		s = "";
	}
	return s;
};

/**
 * Adds class to the element.
 *
 * @param {object} oEl Element
 * @param {string} sClass Class
 */
MyUtils.addClass = function(oEl, sClass) {
	var sClassName = oEl.className;
	if (!new RegExp(("(^|\\s)" + sClass + "(\\s|$)"), "i").test(sClassName)) {
		oEl.className += ((sClassName.length > 0) ? " " : "") + sClass;
	}
};

/**
 * Removes class from the element.
 *
 * @param {object} oEl Element
 * @param {string} sClass Class
 */
MyUtils.removeClass = function(oEl, sClass) {
	oEl.className = MyUtils.multispacekill(oEl.className.replace(sClass, ""));
};
