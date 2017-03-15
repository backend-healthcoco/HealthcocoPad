package com.healthcoco.healthcocoplus.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 08/02/16.
 */
public enum SyncAllType {
    CONTACT(R.string.sync_contact, 1, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.GROUP;
        }
    },
    GROUP(R.string.sync_group, 2, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.HISTORY;
        }
    },
    HISTORY(R.string.sync_history, 3, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.DRUG_CUSTOM;
        }
    },
    DRUG_CUSTOM(R.string.sync_drug, 4, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.FREQUENCY;
        }
    },
    FREQUENCY(R.string.sync_frequency, 5, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.DIRECTION;
        }
    },
    DIRECTION(R.string.sync_direction, 6, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.NOTES_DIAGRAM;
        }
    },
    NOTES_DIAGRAM(R.string.sync_notes_diagram, 7, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTES_DATA;
        }
    },
    CLINICAL_NOTES_DATA(R.string.sync_clinical_notes_data, 8, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.REFERENCES;
        }
    },
    CLINICAL_NOTE_COMPLAINT_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_OBSERVATION_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_OBSERVATION_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_INVESTIGATION_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_INVESTIGATION_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.REFERENCES;
        }
    },

    REFERENCES(R.string.sync_referred_by, 9, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.DRUG_TYPE;
        }
    },
    DRUG_TYPE(R.string.sync_drug_type, 10, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.DRUG_DURATION_UNIT;
        }
    },
    DRUG_DURATION_UNIT(R.string.sync_drug_duration_unit, 11, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return UI_PERMISSIONS;
        }
    },
    UI_PERMISSIONS(R.string.sync_ui_permissions, 12, true) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return null;
        }
    },;

    private boolean isIncludedAsItem = false;
    private SyncAllType nextSyncType = null;
    private int nameId;
    private int position;

    SyncAllType() {

    }

    SyncAllType(int nameId, int position, boolean isIncludedAsItem) {
        this.nameId = nameId;
        this.position = position;
        this.isIncludedAsItem = isIncludedAsItem;
        this.nextSyncType = nextSyncType;
    }

    public int getNameId() {
        return nameId;
    }

    public int getPosition() {
        return position;
    }

    public boolean getIsIncludedAsItem() {
        return isIncludedAsItem;
    }

    public abstract SyncAllType getNextSyncType(SyncAllType syncAllType);
}
