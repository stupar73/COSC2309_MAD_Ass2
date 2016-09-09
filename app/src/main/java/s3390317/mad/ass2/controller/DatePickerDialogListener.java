package s3390317.mad.ass2.controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Assigns the {@code onClick} event of the {@code EditText} passed in to pop-up
 * a {@code DatePickerDialog} and upon a date being selected populates the
 * aforementioned {@code EditText} with that date.
 */
public class DatePickerDialogListener
        implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    private Context context;
    private EditText dateField;
    private Calendar date;
    private DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy",
            Locale.getDefault());

    public DatePickerDialogListener(Context context, EditText dateField)
    {
        this.context = context;
        this.dateField = dateField;
        this.date = Calendar.getInstance();

        this.dateField.setOnClickListener(this);
    }

    public DatePickerDialogListener(Context context, EditText dateField,
                                    Calendar date)
    {
        this.context = context;
        this.dateField = dateField;
        this.dateField.setOnClickListener(this);
        this.date = date;

        this.dateField.setText(formatter.format(date.getTime()));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day)
    {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);

        dateField.setText(formatter.format(date.getTime()));
    }

    @Override
    public void onClick(View view)
    {
        new DatePickerDialog(context, this, date.get(Calendar.YEAR),
                date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
