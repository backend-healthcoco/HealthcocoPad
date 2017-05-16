package com.healthcoco.healthcocopad.listeners;


import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;

/**
 * Created by neha on 13/04/16.
 */
public interface DiagnosticTestItemListener {
    public void onAddClicked(DiagnosticTest diagnosticTest);

    public void onAddedClicked(DiagnosticTest diagnosticTest);

    public DiagnosticTest getDiagnosticTest(String uniqueId);

    public void onDeleteItemClicked(DiagnosticTest diagnosticTest);
}
