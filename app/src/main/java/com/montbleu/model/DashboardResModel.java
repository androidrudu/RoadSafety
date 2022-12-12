package com.montbleu.model;

public class DashboardResModel {
    private LatestDrivingScore latestDrivingScore;

    private String userame;

    private String userId;

    public LatestDrivingScore getLatestDrivingScore ()
    {
        return latestDrivingScore;
    }

    public void setLatestDrivingScore (LatestDrivingScore latestDrivingScore)
    {
        this.latestDrivingScore = latestDrivingScore;
    }

    public String getUserame ()
    {
        return userame;
    }

    public void setUserame (String userame)
    {
        this.userame = userame;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [latestDrivingScore = "+latestDrivingScore+", userame = "+userame+", userId = "+userId+"]";
    }

    public class LatestDrivingScore
    {
        private String monthlyDrivingScoreAvg;

        private String monthlyTotalKiloMeter;

        private String yearlyGoldKiloMeter;

        private String yearlySilverKiloMeter;

        private String yearlySilverHour;

        private String dailyTotalKiloMeter;

        private String weeklyTotalKiloMeter;

        private String yearlyTotalKiloMeter;

        private String yearlyGoldHour;

        private String yearlyBrozneHour;

        private String yearlyDrivingScoreAvg;

        private String dailyDrivingScoreAvg;

        private String weeklyDrivingScoreAvg;

        private String yearlyBrozneKiloMeter;

        private String yearlyPoorKiloMeter;

        private String yearlyPoorRide;

        private String yearlyPoorHour;

        private String yearlyGoldRide;

        private String yearlySilverRide;

        private String yearlyBrozneRide;

        private String yearlyMobileUse;

        public String getYearlyPoorRide() {
            return yearlyPoorRide;
        }

        public void setYearlyPoorRide(String yearlyPoorRide) {
            this.yearlyPoorRide = yearlyPoorRide;
        }

        public String getYearlyPoorHour() {
            return yearlyPoorHour;
        }

        public void setYearlyPoorHour(String yearlyPoorHour) {
            this.yearlyPoorHour = yearlyPoorHour;
        }

        public String getYearlyGoldRide() {
            return yearlyGoldRide;
        }

        public void setYearlyGoldRide(String yearlyGoldRide) {
            this.yearlyGoldRide = yearlyGoldRide;
        }

        public String getYearlySilverRide() {
            return yearlySilverRide;
        }

        public void setYearlySilverRide(String yearlySilverRide) {
            this.yearlySilverRide = yearlySilverRide;
        }

        public String getYearlyBrozneRide() {
            return yearlyBrozneRide;
        }

        public void setYearlyBrozneRide(String yearlyBrozneRide) {
            this.yearlyBrozneRide = yearlyBrozneRide;
        }

        public String getYearlyMobileUse() {
            return yearlyMobileUse;
        }

        public void setYearlyMobileUse(String yearlyMobileUse) {
            this.yearlyMobileUse = yearlyMobileUse;
        }

        public String getMonthlyDrivingScoreAvg ()
        {
            return monthlyDrivingScoreAvg;
        }

        public void setMonthlyDrivingScoreAvg (String monthlyDrivingScoreAvg)
        {
            this.monthlyDrivingScoreAvg = monthlyDrivingScoreAvg;
        }

        public String getMonthlyTotalKiloMeter ()
        {
            return monthlyTotalKiloMeter;
        }

        public void setMonthlyTotalKiloMeter (String monthlyTotalKiloMeter)
        {
            this.monthlyTotalKiloMeter = monthlyTotalKiloMeter;
        }

        public String getYearlyGoldKiloMeter ()
        {
            return yearlyGoldKiloMeter;
        }

        public void setYearlyGoldKiloMeter (String yearlyGoldKiloMeter)
        {
            this.yearlyGoldKiloMeter = yearlyGoldKiloMeter;
        }

        public String getYearlySilverKiloMeter ()
        {
            return yearlySilverKiloMeter;
        }

        public void setYearlySilverKiloMeter (String yearlySilverKiloMeter)
        {
            this.yearlySilverKiloMeter = yearlySilverKiloMeter;
        }

        public String getYearlySilverHour ()
        {
            return yearlySilverHour;
        }

        public void setYearlySilverHour (String yearlySilverHour)
        {
            this.yearlySilverHour = yearlySilverHour;
        }

        public String getDailyTotalKiloMeter ()
        {
            return dailyTotalKiloMeter;
        }

        public void setDailyTotalKiloMeter (String dailyTotalKiloMeter)
        {
            this.dailyTotalKiloMeter = dailyTotalKiloMeter;
        }

        public String getWeeklyTotalKiloMeter ()
        {
            return weeklyTotalKiloMeter;
        }

        public void setWeeklyTotalKiloMeter (String weeklyTotalKiloMeter)
        {
            this.weeklyTotalKiloMeter = weeklyTotalKiloMeter;
        }

        public String getYearlyTotalKiloMeter ()
        {
            return yearlyTotalKiloMeter;
        }

        public void setYearlyTotalKiloMeter (String yearlyTotalKiloMeter)
        {
            this.yearlyTotalKiloMeter = yearlyTotalKiloMeter;
        }

        public String getYearlyGoldHour ()
        {
            return yearlyGoldHour;
        }

        public void setYearlyGoldHour (String yearlyGoldHour)
        {
            this.yearlyGoldHour = yearlyGoldHour;
        }

        public String getYearlyBrozneHour ()
        {
            return yearlyBrozneHour;
        }

        public void setYearlyBrozneHour (String yearlyBrozneHour)
        {
            this.yearlyBrozneHour = yearlyBrozneHour;
        }

        public String getYearlyDrivingScoreAvg ()
        {
            return yearlyDrivingScoreAvg;
        }

        public void setYearlyDrivingScoreAvg (String yearlyDrivingScoreAvg)
        {
            this.yearlyDrivingScoreAvg = yearlyDrivingScoreAvg;
        }

        public String getDailyDrivingScoreAvg ()
        {
            return dailyDrivingScoreAvg;
        }

        public void setDailyDrivingScoreAvg (String dailyDrivingScoreAvg)
        {
            this.dailyDrivingScoreAvg = dailyDrivingScoreAvg;
        }

        public String getWeeklyDrivingScoreAvg ()
        {
            return weeklyDrivingScoreAvg;
        }

        public void setWeeklyDrivingScoreAvg (String weeklyDrivingScoreAvg)
        {
            this.weeklyDrivingScoreAvg = weeklyDrivingScoreAvg;
        }

        public String getYearlyBrozneKiloMeter ()
        {
            return yearlyBrozneKiloMeter;
        }

        public void setYearlyBrozneKiloMeter (String yearlyBrozneKiloMeter)
        {
            this.yearlyBrozneKiloMeter = yearlyBrozneKiloMeter;
        }

        public String getYearlyPoorKiloMeter ()
        {
            return yearlyPoorKiloMeter;
        }

        public void setYearlyPoorKiloMeter (String yearlyPoorKiloMeter)
        {
            this.yearlyPoorKiloMeter = yearlyPoorKiloMeter;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [monthlyDrivingScoreAvg = "+monthlyDrivingScoreAvg+", monthlyTotalKiloMeter = "+monthlyTotalKiloMeter+", yearlyGoldKiloMeter = "+yearlyGoldKiloMeter+", yearlySilverKiloMeter = "+yearlySilverKiloMeter+", yearlySilverHour = "+yearlySilverHour+", dailyTotalKiloMeter = "+dailyTotalKiloMeter+", weeklyTotalKiloMeter = "+weeklyTotalKiloMeter+", yearlyTotalKiloMeter = "+yearlyTotalKiloMeter+", yearlyGoldHour = "+yearlyGoldHour+", yearlyBrozneHour = "+yearlyBrozneHour+", yearlyDrivingScoreAvg = "+yearlyDrivingScoreAvg+", dailyDrivingScoreAvg = "+dailyDrivingScoreAvg+", weeklyDrivingScoreAvg = "+weeklyDrivingScoreAvg+", yearlyBrozneKiloMeter = "+yearlyBrozneKiloMeter+", yearlyPoorKiloMeter = "+yearlyPoorKiloMeter+"]";
        }
    }

}
