package com.exmple.funnyvideo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exmple.funnyvideo.R;

public class SettingActivity extends AppCompatActivity {

    int value1 = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;
    int value5 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.back_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.setting_ly_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, LanguageSelectActivity.class));
            }
        });

        findViewById(R.id.setting_ly_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedBackDialog();
            }
        });

        findViewById(R.id.setting_ly_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.setting_ly_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.setting_ly_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog();
            }
        });
    }

    public void rateDialog() {

        Dialog rateDialog = new Dialog(this);
        rateDialog.requestWindowFeature(1);
        rateDialog.setContentView(R.layout.dialog_rate);
        rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        rateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rateDialog.show();

        TextView rate_text = rateDialog.findViewById(R.id.rate_text);
        ImageView rateStar1 = rateDialog.findViewById(R.id.rateStar1);
        ImageView rateStar2 = rateDialog.findViewById(R.id.rateStar2);
        ImageView rateStar3 = rateDialog.findViewById(R.id.rateStar3);
        ImageView rateStar4 = rateDialog.findViewById(R.id.rateStar4);
        ImageView rateStar5 = rateDialog.findViewById(R.id.rateStar5);
        ImageView emoji_img = rateDialog.findViewById(R.id.emoji_img);
        TextView text_1 = rateDialog.findViewById(R.id.text_1);
        TextView text_2 = rateDialog.findViewById(R.id.text_2);

        rateStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value1 == 0) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar1.setImageResource(R.drawable.rate_star_selected);
                    rateStar2.setImageResource(R.drawable.rate_star_unselected);
                    rateStar3.setImageResource(R.drawable.rate_star_unselected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value1 = 1;
                } else if (value1 == 1) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar1.setImageResource(R.drawable.rate_star_unselected);
                    rateStar2.setImageResource(R.drawable.rate_star_unselected);
                    rateStar3.setImageResource(R.drawable.rate_star_unselected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value5 = 0;
                    value4 = 0;
                    value3 = 0;
                    value2 = 0;
                    value1 = 0;
                }
            }
        });

        rateStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value2 == 0) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar1.setImageResource(R.drawable.rate_star_selected);
                    rateStar2.setImageResource(R.drawable.rate_star_selected);
                    rateStar3.setImageResource(R.drawable.rate_star_unselected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value1 = 1;
                    value2 = 1;
                } else if (value2 == 1) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar2.setImageResource(R.drawable.rate_star_unselected);
                    rateStar3.setImageResource(R.drawable.rate_star_unselected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value5 = 0;
                    value4 = 0;
                    value3 = 0;
                    value2 = 0;
                    value1 = 1;
                }
            }
        });

        rateStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value3 == 0) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar1.setImageResource(R.drawable.rate_star_selected);
                    rateStar2.setImageResource(R.drawable.rate_star_selected);
                    rateStar3.setImageResource(R.drawable.rate_star_selected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value1 = 1;
                    value2 = 1;
                    value3 = 1;
                } else if (value3 == 1) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar3.setImageResource(R.drawable.rate_star_unselected);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value5 = 0;
                    value4 = 0;
                    value3 = 0;
                    value2 = 1;
                    value1 = 1;
                }
            }
        });

        rateStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value4 == 0) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar1.setImageResource(R.drawable.rate_star_selected);
                    rateStar2.setImageResource(R.drawable.rate_star_selected);
                    rateStar3.setImageResource(R.drawable.rate_star_selected);
                    rateStar4.setImageResource(R.drawable.rate_star_selected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_43));
                    text_2.setText(getText(R.string.str_44));
                    rate_text.setText(getText(R.string.str_45));

                    value1 = 1;
                    value2 = 1;
                    value3 = 1;
                    value4 = 1;
                } else if (value4 == 1) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar4.setImageResource(R.drawable.rate_star_unselected);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    text_1.setText(getText(R.string.str_41));
                    text_2.setText(getText(R.string.str_42));
                    rate_text.setText(getText(R.string.str_45));

                    value5 = 0;
                    value4 = 0;
                    value3 = 1;
                    value2 = 1;
                    value1 = 1;
                }
            }
        });

        rateStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value5 == 0) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_1_img);
                    rateStar1.setImageResource(R.drawable.rate_star_selected);
                    rateStar2.setImageResource(R.drawable.rate_star_selected);
                    rateStar3.setImageResource(R.drawable.rate_star_selected);
                    rateStar4.setImageResource(R.drawable.rate_star_selected);
                    rateStar5.setImageResource(R.drawable.rate_star_selected);

                    text_1.setText(getText(R.string.str_43));
                    text_2.setText(getText(R.string.str_44));

                    value1 = 1;
                    value2 = 1;
                    value3 = 1;
                    value4 = 1;
                    value5 = 1;
                } else if (value5 == 1) {
                    emoji_img.setImageResource(R.drawable.rate_emoji_2_img);
                    rateStar5.setImageResource(R.drawable.rate_star_unselected);

                    rate_text.setText(getText(R.string.str_45));

                    value5 = 0;
                    value4 = 1;
                    value3 = 1;
                    value2 = 1;
                    value1 = 1;
                }
            }
        });

        rate_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (value1 == 0) {
                    Toast.makeText(SettingActivity.this, "Please Give Star", Toast.LENGTH_SHORT).show();
                    return;
                }

                rateDialog.dismiss();
                if (value5 == 1) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException unused) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                } else {
                    feedBackDialog();
                }
            }
        });
    }

    public void feedBackDialog() {
        Dialog feedBackDialog = new Dialog(this);
        feedBackDialog.requestWindowFeature(1);
        feedBackDialog.setContentView(R.layout.dialog_feedback);
        feedBackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        feedBackDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        feedBackDialog.show();

        EditText feedback_message_et = feedBackDialog.findViewById(R.id.feedback_message_et);
        TextView send_text = feedBackDialog.findViewById(R.id.send_text);

        feedback_message_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    send_text.setEnabled(true);
                    send_text.setAlpha(1f);
                } else {
                    send_text.setEnabled(false);
                    send_text.setAlpha(0.5f);
                }
            }
        });

        send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                feedBackDialog.dismiss();
                sendFeedback(SettingActivity.this, feedback_message_et.getText().toString());
            }
        });
    }

    public void sendFeedback(Context context, String msg) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", msg);
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"sojitra.jaydip318@gmail.com"});
        intent.setType("text/plain");
        PackageManager packageManager = context.getPackageManager();
        if (isPackageInstalled("com.google.android.gm", packageManager)) {
            intent.setPackage("com.google.android.gm");
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}