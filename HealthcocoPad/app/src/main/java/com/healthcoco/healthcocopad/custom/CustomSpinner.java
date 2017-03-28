package com.healthcoco.healthcocopad.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.SpinnerType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for custom spinner to show prompt, as prompt not showing up in spinner
 */

public class CustomSpinner extends Spinner implements View.OnTouchListener {

    private int promtViewId;
    private int styleDefined;

    public CustomSpinner(Context context) {
        super(context);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOnTouchListener(this);
    }

    @Override
    public void setAdapter(SpinnerAdapter orig) {
        final SpinnerAdapter adapter = newProxy(orig);

        super.setAdapter(adapter);

        try {
            final Method m = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", int.class);
            m.setAccessible(true);
            m.invoke(this, -1);

            final Method n = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", int.class);
            n.setAccessible(true);
            n.invoke(this, -1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected SpinnerAdapter newProxy(SpinnerAdapter obj) {
        return (SpinnerAdapter) java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                new Class[]{SpinnerAdapter.class}, new SpinnerAdapterProxy(obj));
    }

    public void setSpinnerType(SpinnerType spinnerType) {
        if (spinnerType != null) {
            switch (spinnerType) {
                case FREQUENCY_DOSAGE:
                case DIRECTIONS:
                case DURATION_UNIT:
                    this.promtViewId = R.layout.spinner_prompt_view_add_drug_detail;
                    break;
                case DRUG_TYPE:
                    this.promtViewId = R.layout.spinner_prompt_add_new_drug;
                    break;
                case APPOINTMENT_SLOT:
                    styleDefined = R.style.add_appointment_details_input_edit_text_style;
                    break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Intercepts getView() to display the prompt if position < 0
     */
    protected class SpinnerAdapterProxy implements InvocationHandler {

        protected SpinnerAdapter obj;
        protected Method getView;

        protected SpinnerAdapterProxy(SpinnerAdapter obj) {
            this.obj = obj;
            try {
                this.getView = SpinnerAdapter.class.getMethod("getView", int.class, View.class, ViewGroup.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                return m.equals(getView) && (Integer) (args[0]) < 0
                        ? getView((Integer) args[0], (View) args[1], (ViewGroup) args[2]) : m.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        protected View getView(int position, View convertView, ViewGroup parent) throws IllegalAccessException {
            if (position < 0) {
                final TextView v;
                if (promtViewId > 0) {
                    v = (TextView) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(promtViewId, parent, false);
                } else
                    v = (TextView) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.spinner_common_item_top, parent, false);
                if (styleDefined > 0)
                    v.setTextAppearance(getContext(), styleDefined);
                v.setGravity(Gravity.CENTER_VERTICAL);
                v.setHint(getPrompt());

                return v;
            }
            return obj.getView(position, convertView, parent);
        }
    }

}
