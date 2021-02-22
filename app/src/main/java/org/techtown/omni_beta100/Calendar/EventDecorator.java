package org.techtown.omni_beta100.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.techtown.omni_beta100.R;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;
    public EventDecorator(Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.calendar_background);

        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day){
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);

        view.addSpan(new ForegroundColorSpan(Color.parseColor("#8795ff")));
    }



}
