package com.jiezhi.lib.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jiezhi.lib.model.StudentInfo;
import com.umeng.analytics.MobclickAgent;

public class StudentInfoActivity extends FinalActivity {

	@ViewInject(id = R.id.nameText)
	TextView nameText;
	@ViewInject(id = R.id.numberText)
	TextView numberText;
	@ViewInject(id = R.id.sexText)
	TextView sexText;
	@ViewInject(id = R.id.educationText)
	TextView educationText;
	@ViewInject(id = R.id.workPlaceText)
	TextView workPlaceText;
	@ViewInject(id = R.id.cellphoneText)
	TextView telText;
	@ViewInject(id = R.id.gradeText)
	TextView sumBorrowedText;

	StudentInfo mStudent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentinfo);

		FinalDb db = FinalDb.create(this, "stu_info");
		StudentInfo mStudent = db.findById(1, StudentInfo.class);
		if (mStudent != null)
			System.out.println(mStudent.toString());
		String number = mStudent.getNumber();
		numberText.setText(number);
		nameText.setText(mStudent.getName());
		sexText.setText(mStudent.getSex());
		telText.setText(mStudent.getTel());
		educationText.setText(mStudent.getEducation());
		workPlaceText.setText(mStudent.getWockPlace());
		sumBorrowedText.setText(mStudent.getGrade());
		int toExpire = Integer.parseInt(mStudent.getToExpire());
		if (toExpire > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("警告");
			builder.setMessage("您有" + toExpire + "本书在5天内即将过期，要查看吗？");
			builder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent i = new Intent();
							i.setClass(getApplicationContext(),
									BorrowedActivity.class);
							startActivity(i);
							finish();
						}
					});
			builder.setNegativeButton("否",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			AlertDialog ad = builder.create();
			ad.show();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
