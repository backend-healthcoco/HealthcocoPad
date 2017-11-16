package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 20-07-2017.
 */

public enum DentalChartType {

    QUADRANT_ONE_ADULT(new ArrayList<Object>() {
        {
            add(Q1.TOOTH18);
            add(Q1.TOOTH17);
            add(Q1.TOOTH16);
            add(Q1.TOOTH15);
            add(Q1.TOOTH14);
            add(Q1.TOOTH13);
            add(Q1.TOOTH12);
            add(Q1.TOOTH11);
        }
    }),

    QUADRANT_TWO_ADULT(new ArrayList<Object>() {
        {
            add(Q2.TOOTH21);
            add(Q2.TOOTH22);
            add(Q2.TOOTH23);
            add(Q2.TOOTH24);
            add(Q2.TOOTH25);
            add(Q2.TOOTH26);
            add(Q2.TOOTH27);
            add(Q2.TOOTH28);
        }
    }

    ),

    QUADRANT_THREE_ADULT(new ArrayList<Object>() {
        {
            add(Q3.TOOTH48);
            add(Q3.TOOTH47);
            add(Q3.TOOTH46);
            add(Q3.TOOTH45);
            add(Q3.TOOTH44);
            add(Q3.TOOTH43);
            add(Q3.TOOTH42);
            add(Q3.TOOTH41);
        }
    }

    ),

    QUADRANT_FOUR_ADULT(new ArrayList<Object>() {
        {
            add(Q4.TOOTH31);
            add(Q4.TOOTH32);
            add(Q4.TOOTH33);
            add(Q4.TOOTH34);
            add(Q4.TOOTH35);
            add(Q4.TOOTH36);
            add(Q4.TOOTH37);
            add(Q4.TOOTH38);
        }
    }

    ),

    QUADRANT_ONE_BABY(new ArrayList<Object>() {
        {
            add(QB1.TOOTH55);
            add(QB1.TOOTH54);
            add(QB1.TOOTH53);
            add(QB1.TOOTH52);
            add(QB1.TOOTH51);
        }
    }

    ),

    QUADRANT_TWO_BABY(new ArrayList<Object>() {
        {
            add(QB2.TOOTH61);
            add(QB2.TOOTH62);
            add(QB2.TOOTH63);
            add(QB2.TOOTH64);
            add(QB2.TOOTH65);
        }
    }

    ),

    QUADRANT_THREE_BABY(new ArrayList<Object>() {
        {
            add(QB3.TOOTH85);
            add(QB3.TOOTH84);
            add(QB3.TOOTH83);
            add(QB3.TOOTH82);
            add(QB3.TOOTH81);
        }
    }

    ),

    QUADRANT_FOUR_BABY(new ArrayList<Object>() {
        {
            add(QB4.TOOTH71);
            add(QB4.TOOTH72);
            add(QB4.TOOTH73);
            add(QB4.TOOTH74);
            add(QB4.TOOTH75);
        }
    }

    );

    //Adult mAx and Minium toothNoumbers
    public static final int MIN_ADULT_TOOTH_NUMBER = 11;
    public static final int MAX_ADULT_TOOTH_NUMBER = 48;
    public static final int MIN_CHILD_TOOTH_NUMBER = 55;
    public static final int MAX_CHILD_TOOTH_NUMBER = 85;
    private ArrayList<Object> quadrantType;

    DentalChartType(ArrayList<Object> quadrantType) {
        this.quadrantType = quadrantType;
    }

    public ArrayList<Object> getQuadrantType() {
        return quadrantType;
    }

    public void setQuadrantType(ArrayList<Object> quadrantType) {
        this.quadrantType = quadrantType;
    }

    public enum Q1 {
        TOOTH18(18, R.drawable.eighteen),
        TOOTH17(17, R.drawable.seventeen),
        TOOTH16(16, R.drawable.sixteen),
        TOOTH15(15, R.drawable.fifteen),
        TOOTH14(14, R.drawable.fourteen),
        TOOTH13(13, R.drawable.thirteen),
        TOOTH12(12, R.drawable.twelve),
        TOOTH11(11, R.drawable.eleven);

        private int toothNo;
        private int resourceId;

        Q1(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static Q1 getValue(int toothNo) {
            for (Q1 q1 :
                    Q1.values()) {
                if (q1.getToothNo() == toothNo)
                    return q1;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum Q2 {
        TOOTH21(21, R.drawable.twenty_one),
        TOOTH22(22, R.drawable.twenty_two),
        TOOTH23(23, R.drawable.twenty_three),
        TOOTH24(24, R.drawable.twenty_four),
        TOOTH25(25, R.drawable.twenty_five),
        TOOTH26(26, R.drawable.twenty_six),
        TOOTH27(27, R.drawable.twenty_seven),
        TOOTH28(28, R.drawable.twenty_eight);

        private int toothNo;
        private int resourceId;

        Q2(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static Q2 getValue(int toothNo) {
            for (Q2 q2 :
                    Q2.values()) {
                if (q2.getToothNo() == toothNo)
                    return q2;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum Q3 {
        TOOTH48(48, R.drawable.fourty_eight),
        TOOTH47(47, R.drawable.fourty_seven),
        TOOTH46(46, R.drawable.fourty_six),
        TOOTH45(45, R.drawable.fourty_five),
        TOOTH44(44, R.drawable.fourty_four),
        TOOTH43(43, R.drawable.fourty_three),
        TOOTH42(42, R.drawable.fourty_two),
        TOOTH41(41, R.drawable.fourty_one);

        private int toothNo;
        private int resourceId;

        Q3(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static Q3 getValue(int toothNo) {
            for (Q3 q3 :
                    Q3.values()) {
                if (q3.getToothNo() == toothNo)
                    return q3;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum Q4 {
        TOOTH31(31, R.drawable.thirty_one),
        TOOTH32(32, R.drawable.thirty_two),
        TOOTH33(33, R.drawable.thirty_three),
        TOOTH34(34, R.drawable.thirty_four),
        TOOTH35(35, R.drawable.thirty_five),
        TOOTH36(36, R.drawable.thirty_six),
        TOOTH37(37, R.drawable.thirty_seven),
        TOOTH38(38, R.drawable.thirty_eight);

        private int toothNo;
        private int resourceId;

        Q4(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static Q4 getValue(int toothNo) {
            for (Q4 q4 :
                    Q4.values()) {
                if (q4.getToothNo() == toothNo)
                    return q4;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    //
    public enum QB1 {
        TOOTH55(55, R.drawable.fifteen),
        TOOTH54(54, R.drawable.fourteen),
        TOOTH53(53, R.drawable.thirteen),
        TOOTH52(52, R.drawable.twelve),
        TOOTH51(51, R.drawable.eleven);

        private int toothNo;
        private int resourceId;

        QB1(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static QB1 getValue(int toothNo) {
            for (QB1 qb1 :
                    QB1.values()) {
                if (qb1.getToothNo() == toothNo)
                    return qb1;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum QB2 {
        TOOTH61(61, R.drawable.twenty_one),
        TOOTH62(62, R.drawable.twenty_two),
        TOOTH63(63, R.drawable.twenty_three),
        TOOTH64(64, R.drawable.twenty_four),
        TOOTH65(65, R.drawable.twenty_five);

        private int toothNo;
        private int resourceId;

        QB2(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static QB2 getValue(int toothNo) {
            for (QB2 qb2 :
                    QB2.values()) {
                if (qb2.getToothNo() == toothNo)
                    return qb2;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum QB3 {
        TOOTH85(85, R.drawable.fourty_five),
        TOOTH84(84, R.drawable.fourty_four),
        TOOTH83(83, R.drawable.fourty_three),
        TOOTH82(82, R.drawable.fourty_two),
        TOOTH81(81, R.drawable.fourty_one);

        private int toothNo;
        private int resourceId;

        QB3(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static QB3 getValue(int toothNo) {
            for (QB3 qb3 :
                    QB3.values()) {
                if (qb3.getToothNo() == toothNo)
                    return qb3;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public enum QB4 {
        TOOTH71(71, R.drawable.thirty_one),
        TOOTH72(72, R.drawable.thirty_two),
        TOOTH73(73, R.drawable.thirty_three),
        TOOTH74(74, R.drawable.thirty_four),
        TOOTH75(75, R.drawable.thirty_five);

        private int toothNo;
        private int resourceId;

        QB4(int toothNo, int resourceId) {
            this.toothNo = toothNo;
            this.resourceId = resourceId;
        }

        public static QB4 getValue(int toothNo) {
            for (QB4 qb4 :
                    QB4.values()) {
                if (qb4.getToothNo() == toothNo)
                    return qb4;
            }
            return null;
        }

        public int getToothNo() {
            return toothNo;
        }

        public void setToothNo(int toothNo) {
            this.toothNo = toothNo;
        }

        public int getResourceId() {
            return resourceId;
        }

        public void setResourceId(int resourceId) {
            this.resourceId = resourceId;
        }
    }

}