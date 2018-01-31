package com.healthcoco.healthcocopad.enums;


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
            return SyncAllType.CLINICAL_NOTE_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PRESENT_COMPLAINT_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PRESENT_COMPLAINT_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_OBSTETRIC_HISTORY_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_OBSTETRIC_HISTORY_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_GENERAL_EXAMINATION_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_GENERAL_EXAMINATION_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_SYSTEMIC_EXAMINATION_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_SYSTEMIC_EXAMINATION_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PROVISIONAL_DIAGNOSIS_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_ECG_DETAILS_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_ECG_DETAILS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_ECHO_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_ECHO_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_X_RAY_DETAILS_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_X_RAY_DETAILS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_HOLTER_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_HOLTER_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PA_SUGGESTIONS;
        }
    }, CLINICAL_NOTE_PA_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PV_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PV_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PS_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_EAR_EXAM_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_EAR_EXAM_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_NECK_EXAM_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_NECK_EXAM_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_NOSE_EXAM_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_NOSE_EXAM_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PC_EARS_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PC_EARS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PC_NOSE_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PC_NOSE_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PC_ORAL_CAVITY_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PC_ORAL_CAVITY_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PC_THROAT_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PC_THROAT_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_PROCEDURE_NOTE_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_PROCEDURE_NOTE_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_INDICATION_OF_USG_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_INDICATION_OF_USG_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_NOTES_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_NOTES_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_MENSTRUAL_HISTORY_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_MENSTRUAL_HISTORY_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS;
        }
    },
    CLINICAL_NOTE_DIAGNOSIS_SUGGESTIONS(0, 0, false) {
        @Override
        public SyncAllType getNextSyncType(SyncAllType syncAllType) {
            return SyncAllType.CLINICAL_NOTE_ADVICE_SUGGESTIONS;
        }
    },

    CLINICAL_NOTE_ADVICE_SUGGESTIONS(0, 0, false) {
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
