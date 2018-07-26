package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

public enum InvestigationSubType {

    AEC("AEC", R.string.absolute_eosinophil_count, R.string.absolute_eosinophil_count_range),
    HAEMOGLOBIN("HAEMOGLOBIN", R.string.haemoglobin, R.string.haemoglobin_range),
    TWC("TWC", R.string.total_wcb_count, R.string.total_wcb_count_range),
    HAEMATOCRIT("HAEMATOCRIT", R.string.haematocrit, R.string.haematocrit_range),
    NEUTROPHILS("NEUTROPHILS", R.string.neutrophils, R.string.neutrophils),
    LYMPHOCYTES("LYMPHOCYTES", R.string.lymphocytes, R.string.lymphocytes_range),
    EOSINOPHILS("EOSINOPHILS", R.string.eosinophils, R.string.eosinophils_range),
    MONOCYTES("MONOCYTES", R.string.monocytes, R.string.monocytes_range),
    BASOPHILS("BASOPHILS", R.string.basophils, R.string.basophils_range),
    RBC("RBC", R.string.rbc_red_blood_cells, R.string.rbc_red_blood_cells_range),
    ESR("ESR", R.string.erythrocyte_sedimentationrate, R.string.erythrocyte_sedimentationrate_range),
    RBCS("RBCS", R.string.rbcs, R.string.rbs_range),
    WBCS("WBCS", R.string.wbcs, R.string.wbcs_range),
    PLATELETS("PLATELETS", R.string.platelets, R.string.platelets_range),
    HAEMOPARASITES("HAEMOPARASITES", R.string.haemoparasites, R.string.haemoparasites_range),
    IMPRESSION("IMPRESSION", R.string.impression, R.string.impression_range),
    MCV("MCV", R.string.mean_corpuscular_volume, R.string.mean_corpuscular_volume_range),
    MCH("MCH", R.string.mean_corpuscular_haemoglobin, R.string.mean_corpuscular_haemoglobin_range),
    MCHC("MCHC", R.string.mean_corpuscular_haemoglobin_concentration, R.string.mean_corpuscular_haemoglobin_concentration_range),
    FSB("FSB", R.string.fasting_blood_sugar, R.string.fasting_blood_sugar_range),
    FUS("FUS", R.string.fasting_urine_sugar, R.string.fasting_urine_sugar_range),
    PPBS("PPBS", R.string.post_prandial_blood_sugar, R.string.post_prandial_blood_sugar_range),
    PPUS("PPUS", R.string.post_prandial_urine_sugar, R.string.post_prandial_urine_sugar_range),
    GH("GH", R.string.glycosylated_haemoglobin, R.string.glycosylated_haemoglobin_range),
    MBG("MBG", R.string.mean_blood_glucose, R.string.mean_blood_glucose_range),
    RBS("RBS", R.string.rbs, R.string.rbs_range),
    RUS("RUS", R.string.random_urine_sugar, R.string.random_urine_sugar_range),
    KETONE("KETONE", R.string.ketone, R.string.ketone_range),
    PROTEIN("PROTEIN", R.string.protein, R.string.protein_range),
    TC("TC", R.string.total_cholesterol, R.string.total_cholesterol_range),
    SHC("SHC", R.string.serum_hdl_cholesterol, R.string.serum_hdl_cholesterol_range),
    ST("ST", R.string.serum_triglycerides, R.string.serum_triglycerides_range),
    SLC("SLC", R.string.serum_ldl_cholesterol, R.string.serum_ldl_cholesterol_range),
    SVC("SVC", R.string.serum_vldl_cholesterol, R.string.serum_vldl_cholesterol_range),
    LDL("LDL", R.string.ldl_cholesterol, R.string.ldl_cholesterol_range),
    TRIGLYCERIDES("TRIGLYCERIDES", R.string.trigkycerides, R.string.trigkycerides_range),
    NHC("NHC", R.string.non_hdl_cholesterol, R.string.non_hdl_cholesterol_range),
    BU("BU", R.string.blood_urea, R.string.blood_urea_range),
    SC("SC", R.string.serum_creatinine, R.string.serum_creatinine_range),
    SS("SS", R.string.serum_sodium, R.string.serum_sodium_range),
    SBT("SBT", R.string.serum_bilirubin_total, R.string.serum_bilirubin_total_range),
    SBD("SBD", R.string.serum_bilirubin_direct, R.string.serum_bilirubin_direct_range),
    BI("BI", R.string.bilirubin_indirect, R.string.bilirubin_indirect_range);

    private final String value;
    private int textId;
    private int hintId;

    private InvestigationSubType(String value, int textId, int hintId) {
        this.value = value;
        this.textId = textId;
        this.hintId = hintId;
    }

    public int getTextId() {
        return textId;
    }

    public int getHintId() {
        return hintId;
    }

    public String getValue() {
        return value;
    }


}
