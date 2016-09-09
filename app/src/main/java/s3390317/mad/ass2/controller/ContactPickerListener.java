package s3390317.mad.ass2.controller;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;

import s3390317.mad.ass2.view.model.IntentRequestCodes;

/**
 * Starts the contact picker activity which sets the result as the selected
 * contact, this can be captured using {@code onActivityResult()}.
 */
public class ContactPickerListener implements View.OnClickListener
{
    private Activity activity;

    public ContactPickerListener(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onClick(View view)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(
                contactPickerIntent, IntentRequestCodes.PICK_CONTACTS_REQUEST);
    }
}
