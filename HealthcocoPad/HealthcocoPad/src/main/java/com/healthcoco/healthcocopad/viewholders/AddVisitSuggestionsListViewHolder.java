package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 15/04/17.
 */

public class AddVisitSuggestionsListViewHolder extends HealthCocoViewHolder {
    private Object objData;
    private TextView tvName;
    private TextView tvGenericName;
    private static final String GENERIC_NAME_SEPARATOR = ",";

    public AddVisitSuggestionsListViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.objData = object;
    }

    @Override
    public void applyData() {
        String text = "";
        if (objData instanceof DrugsListSolrResponse)
            text = ((DrugsListSolrResponse) objData).getDrugName();
        else if (objData instanceof DiagnosticTest)
            text = ((DiagnosticTest) objData).getTestName();
        else if (objData instanceof PresentComplaintSuggestions)
            text = ((PresentComplaintSuggestions) objData).getPresentComplaint();
        else if (objData instanceof ComplaintSuggestions)
            text = ((ComplaintSuggestions) objData).getComplaint();
        else if (objData instanceof HistoryPresentComplaintSuggestions)
            text = ((HistoryPresentComplaintSuggestions) objData).getPresentComplaintHistory();
        else if (objData instanceof MenstrualHistorySuggestions)
            text = ((MenstrualHistorySuggestions) objData).getMenstrualHistory();
        else if (objData instanceof ObstetricHistorySuggestions)
            text = ((ObstetricHistorySuggestions) objData).getObstetricHistory();
        else if (objData instanceof GeneralExaminationSuggestions)
            text = ((GeneralExaminationSuggestions) objData).getGeneralExam();
        else if (objData instanceof SystemicExaminationSuggestions)
            text = ((SystemicExaminationSuggestions) objData).getSystemExam();
        else if (objData instanceof ObservationSuggestions)
            text = ((ObservationSuggestions) objData).getObservation();
        else if (objData instanceof InvestigationSuggestions)
            text = ((InvestigationSuggestions) objData).getInvestigation();
        else if (objData instanceof ProvisionalDiagnosisSuggestions)
            text = ((ProvisionalDiagnosisSuggestions) objData).getProvisionalDiagnosis();
        else if (objData instanceof DiagnosisSuggestions)
            text = ((DiagnosisSuggestions) objData).getDiagnosis();
        else if (objData instanceof NotesSuggestions)
            text = ((NotesSuggestions) objData).getNote();
        else if (objData instanceof EcgDetailSuggestions)
            text = ((EcgDetailSuggestions) objData).getEcgDetails();
        else if (objData instanceof EchoSuggestions)
            text = ((EchoSuggestions) objData).getEcho();
        else if (objData instanceof XrayDetailSuggestions)
            text = ((XrayDetailSuggestions) objData).getxRayDetails();
        else if (objData instanceof PaSuggestions)
            text = ((PaSuggestions) objData).getPa();
        else if (objData instanceof PvSuggestions)
            text = ((PvSuggestions) objData).getPv();
        else if (objData instanceof PsSuggestions)
            text = ((PsSuggestions) objData).getPs();
        else if (objData instanceof IndicationOfUsgSuggestions)
            text = ((IndicationOfUsgSuggestions) objData).getIndicationOfUSG();
        else if (objData instanceof HolterSuggestions)
            text = ((HolterSuggestions) objData).getHolter();
        tvName.setText(text);
        if (objData instanceof DrugsListSolrResponse) {
            String genericNamesFormatted = "";
            if (!Util.isNullOrEmptyList(((DrugsListSolrResponse) objData).getGenericNames())) {
                for (GenericName genericName : ((DrugsListSolrResponse) objData).getGenericNames()) {
                    int index = ((DrugsListSolrResponse) objData).getGenericNames().indexOf(genericName);
                    genericNamesFormatted = genericNamesFormatted + " " + genericName.getName();
                    if (index != ((DrugsListSolrResponse) objData).getGenericNames().size() - 1)
                        genericNamesFormatted = genericNamesFormatted + GENERIC_NAME_SEPARATOR;
                }
            }
            tvGenericName.setText(genericNamesFormatted);
            tvGenericName.setVisibility(View.VISIBLE);
        } else tvGenericName.setVisibility(View.GONE);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_add_visits_sugestions, null);
        tvName = (TextView) contentView.findViewById(R.id.tv_name);
        tvGenericName = (TextView) contentView.findViewById(R.id.tv_generic_name);
        contentView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, GridView.AUTO_FIT));
        return contentView;
    }
}
