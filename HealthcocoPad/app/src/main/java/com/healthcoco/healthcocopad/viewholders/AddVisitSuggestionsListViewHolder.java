package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;

/**
 * Created by neha on 15/04/17.
 */

public class AddVisitSuggestionsListViewHolder extends HealthCocoViewHolder {
    private HealthCocoActivity mActivity;
    private Object objData;
    private TextView tvName;
    private View contentView;

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
        else if (objData instanceof ComplaintSuggestions)
            text = ((ComplaintSuggestions) objData).getComplaint();
        else if (objData instanceof ObservationSuggestions)
            text = ((ObservationSuggestions) objData).getObservation();
        else if (objData instanceof InvestigationSuggestions)
            text = ((InvestigationSuggestions) objData).getInvestigation();
        else if (objData instanceof DiagnosisSuggestions)
            text = ((DiagnosisSuggestions) objData).getDiagnosis();
        tvName.setText(text);
    }

    @Override
    public View getContentView() {
        contentView = inflater.inflate(R.layout.list_item_add_visits_sugestions, null);
        tvName = (TextView) contentView.findViewById(R.id.tv_name);
        return contentView;
    }
}
