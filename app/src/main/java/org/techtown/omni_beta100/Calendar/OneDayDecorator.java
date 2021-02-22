package org.techtown.omni_beta100.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.techtown.omni_beta100.R;

import java.util.Calendar;
import java.util.Date;

public class OneDayDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private CalendarDay date;

    public OneDayDecorator(CalendarDay day, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.calendar_today_background);
        date = day;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")));


    }


    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
