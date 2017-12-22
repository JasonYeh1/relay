package io.forsta.securesms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.forsta.securesms.recipients.Recipient;
import io.forsta.securesms.recipients.Recipients;
import io.forsta.securesms.util.ViewUtil;

public class ConversationTitleView extends LinearLayout {

  private static final String TAG = ConversationTitleView.class.getSimpleName();

  private TextView  title;
  private TextView  subtitle;

  public ConversationTitleView(Context context) {
    this(context, null);
  }

  public ConversationTitleView(Context context, AttributeSet attrs) {
    super(context, attrs);

  }

  @Override
  public void onFinishInflate() {
    super.onFinishInflate();

    this.title    = (TextView) findViewById(R.id.title);
    this.subtitle = (TextView) findViewById(R.id.subtitle);

    ViewUtil.setTextViewGravityStart(this.title, getContext());
    ViewUtil.setTextViewGravityStart(this.subtitle, getContext());
  }

  public void setTitle(@Nullable Recipients recipients, int threadType) {
    if      (recipients == null)             setComposeTitle();
    else if (recipients.isSingleRecipient()) setRecipientTitle(recipients.getPrimaryRecipient(), threadType);
    else                                     setRecipientsTitle(recipients, threadType);

    if (recipients != null && recipients.isBlocked()) {
      title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_block_white_18dp, 0, 0, 0);
    } else if (recipients != null && recipients.isMuted()) {
      title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_volume_off_white_18dp, 0, 0, 0);
    } else {
      title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }
  }

  private void setComposeTitle() {
    this.title.setText(R.string.ConversationActivity_compose_message);
    this.subtitle.setText(null);
    this.subtitle.setVisibility(View.GONE);
  }

  private void setRecipientTitle(Recipient recipient, int threadType) {
    if (!recipient.isGroupRecipient()) {
      if (TextUtils.isEmpty(recipient.getName())) {
        this.title.setText(recipient.getNumber());
        this.subtitle.setText(null);
        this.subtitle.setVisibility(View.GONE);
      } else {
        this.title.setText(recipient.getName());
        this.subtitle.setText(recipient.getNumber());
        // Remove subtitle phone from actionbar display
        this.subtitle.setVisibility(View.GONE);
      }
    } else {
      String groupName = (!TextUtils.isEmpty(recipient.getName())) ?
                         recipient.getName() :
                         getContext().getString(R.string.ConversationActivity_unnamed_group);

      this.title.setText(groupName);
      this.subtitle.setText(null);
      this.subtitle.setVisibility(View.GONE);
    }
    if (threadType == 1) {
      this.title.setVisibility(VISIBLE);
      this.title.setText("Announcment");
    }
  }

  private void setRecipientsTitle(Recipients recipients, int threadType) {
    int size = recipients.getRecipientsList().size();

    title.setText(getContext().getString(R.string.ConversationActivity_group_conversation));
    subtitle.setText(getContext().getResources().getQuantityString(R.plurals.ConversationActivity_d_recipients_in_group, size, size));
    subtitle.setVisibility(View.VISIBLE);
    if (threadType == 1) {
      this.subtitle.setText("Announcment");
    }
  }

  public void setForstaTitle(String forstaTitle, int threadType) {
    title.setText(forstaTitle);
    if (threadType == 1) {
      this.subtitle.setVisibility(VISIBLE);
      subtitle.setText("Announcement");
    }
  }
}
