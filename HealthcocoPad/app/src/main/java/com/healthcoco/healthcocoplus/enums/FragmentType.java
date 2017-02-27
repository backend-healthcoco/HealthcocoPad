package com.healthcoco.healthcocoplus.enums;


import com.healthcoco.healthcocopad.R;

public enum FragmentType {
    CONTACTS(MenuType.SEPARATOR, R.string.contacts, R.drawable.ic_action_call, ActionbarLeftRightActionTypeDrawables.WITH_FILTER),
    CLINIC_PROFILE(MenuType.SEPARATOR, R.string.clinic_proifle, R.drawable.ic_action_clinic, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    PROFILE(MenuType.SEPARATOR, R.string.profile, R.drawable.ic_action_profileicon, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    CALENDAR(MenuType.SEPARATOR, R.string.calendar, R.drawable.ic_action_calender, ActionbarLeftRightActionTypeDrawables.WITH_ADD),
    MAIL(MenuType.SEPARATOR, R.string.mail, R.drawable.menu_mail_selector, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    ISSUE_TRACKER(MenuType.SEPARATOR, R.string.issue_tracker, R.drawable.ic_action_issue_tracker, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    SETTINGS(MenuType.SEPARATOR, R.string.settings, R.drawable.ic_action_setting, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    HELP_IMPROVE(MenuType.SEPARATOR, R.string.help_us_to_improve, R.drawable.ic_action_help, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION),
    SYNC(MenuType.SEPARATOR, R.string.sync, R.drawable.ic_action_sync, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION);

    private final ActionbarLeftRightActionTypeDrawables rigthActionType;
    private int titleId;
    private MenuType menuType;
    private int drawableId;

    FragmentType(MenuType menuType, int titleId, int drawableId, ActionbarLeftRightActionTypeDrawables rigthActionType) {
        this.titleId = titleId;
        this.menuType = menuType;
        this.drawableId = drawableId;
        this.rigthActionType = rigthActionType;
    }

    public int getTitleId() {
        return titleId;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public ActionbarLeftRightActionTypeDrawables getRightActionType() {
        return rigthActionType;
    }
}
