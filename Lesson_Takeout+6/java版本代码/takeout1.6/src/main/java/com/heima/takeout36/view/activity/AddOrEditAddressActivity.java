package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heima.takeout36.R;
import com.heima.takeout36.model.dao.AddressDao;
import com.heima.takeout36.model.dao.RecepitAddress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heima.takeout36.R.id.rl_phone_other;

/**
 * Created by lidongzhi on 2017/6/9.
 */
public class AddOrEditAddressActivity extends Activity {
    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ib_delete)
    ImageButton mIbDelete;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.rb_man)
    RadioButton mRbMan;
    @Bind(R.id.rb_women)
    RadioButton mRbWomen;
    @Bind(R.id.rg_sex)
    RadioGroup mRgSex;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.ib_delete_phone)
    ImageButton mIbDeletePhone;
    @Bind(R.id.ib_add_phone_other)
    ImageButton mIbAddPhoneOther;
    @Bind(R.id.et_phone_other)
    EditText mEtPhoneOther;
    @Bind(R.id.ib_delete_phone_other)
    ImageButton mIbDeletePhoneOther;
    @Bind(rl_phone_other)
    RelativeLayout mRlPhoneOther;
    @Bind(R.id.et_receipt_address)
    EditText mEtReceiptAddress;
    @Bind(R.id.et_detail_address)
    EditText mEtDetailAddress;
    @Bind(R.id.tv_label)
    TextView mTvLabel;
    @Bind(R.id.ib_select_label)
    ImageView mIbSelectLabel;
    @Bind(R.id.bt_ok)
    Button mBtOk;
    @Bind(R.id.btn_location_address)
    Button mBtnLocationAddress;
    private AddressDao mAddressDao;
    private RecepitAddress mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_receipt_address);
        ButterKnife.bind(this);
        processIntent();
        mAddressDao = new AddressDao(this);

        mEtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //有焦点

                } else {

                }
            }
        });

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    //需要显示X号
                    mIbDeletePhone.setVisibility(View.VISIBLE);
                } else {
                    //隐藏X号
                    mIbDeletePhone.setVisibility(View.INVISIBLE);
                }
            }
        });

        mEtPhoneOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    //需要显示X号
                    mIbDeletePhoneOther.setVisibility(View.VISIBLE);
                } else {
                    //隐藏X号
                    mIbDeletePhoneOther.setVisibility(View.GONE);
                }
            }
        });
    }

    private void processIntent() {
        if (getIntent() != null) {
            mAddress = (RecepitAddress) getIntent().getSerializableExtra("address");
            if (mAddress != null) {
                //也可以删除
                mIbDelete.setVisibility(View.VISIBLE);
                mIbDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDeleteDialog();
                    }
                });
                //更新
                mTvTitle.setText("更新地址");
                mEtName.setText(mAddress.getName());
                String sex = mAddress.getSex();
                if ("先生".equals(sex)) {
                    mRbMan.setChecked(true);
                } else {
                    mRbWomen.setChecked(true);
                }
                mEtPhone.setText(mAddress.getPhone());
                mEtPhoneOther.setText(mAddress.getPhoneOther());
                mEtReceiptAddress.setText(mAddress.getAddress());
                mEtDetailAddress.setText(mAddress.getDetailAddress());
                mTvLabel.setText(mAddress.getSelectLabel());
                mTvLabel.setTextColor(Color.BLACK);

            } else {
                mTvTitle.setText("新增地址");
            }
        }
    }

    private void alertDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除这个地址么？");
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAddressDao.deleteAddress(mAddress);
                Toast.makeText(AddOrEditAddressActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("不，保留此地址", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            String title = data.getStringExtra("title");
            String address = data.getStringExtra("address");
            mEtReceiptAddress.setText(title);
            mEtDetailAddress.setText(address);
        }
    }

    @OnClick({R.id.btn_location_address, R.id.ib_back, R.id.ib_delete, R.id.ib_delete_phone, R.id.ib_add_phone_other, R.id.ib_delete_phone_other, R.id.ib_select_label, R.id.bt_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_location_address:
                Intent intent = new Intent(this, AroundSearchActivity.class);
                startActivityForResult(intent, 1001);
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_delete:
                break;
            case R.id.ib_delete_phone:
                //删除电话
                mEtPhone.setText("");
                break;
            case R.id.ib_add_phone_other:
                //添加备用电话
                mRlPhoneOther.setVisibility(View.VISIBLE);
                break;
            case R.id.ib_delete_phone_other:
                //删除备选电话
                mEtPhoneOther.setText("");
                break;
            case R.id.ib_select_label:
                //弹出选择标签
                alertSelectLabelDialog();
                break;
            case R.id.bt_ok:
                boolean isOk = checkReceiptAddressInfo();
                if (isOk) {
                    if (mAddress != null) {
                        //更新
                        String name = mEtName.getText().toString().trim();
                        String sex = "女士";
                        if (mRbMan.isChecked()) {
                            sex = "先生";
                        }
                        String phone = mEtPhone.getText().toString().trim();
                        String phoneOther = mEtPhoneOther.getText().toString().trim();
                        String address = mEtReceiptAddress.getText().toString().trim();
                        String detailAddress = mEtDetailAddress.getText().toString().trim();
                        String selectLabel = mTvLabel.getText().toString().trim();
                        mAddress.setName(name);
                        mAddress.setSex(sex);
                        mAddress.setPhone(phone);
                        mAddress.setPhoneOther(phoneOther);
                        mAddress.setAddress(address);
                        mAddress.setSelectLabel(selectLabel);
                        mAddress.setDetailAddress(detailAddress);
                        boolean ok = mAddressDao.updateAddress(mAddress);
                        if (ok) {
                            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "请仔细检查您填写的资料", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //把地址保存到本地数据库
//                    int id, String name, String sex, String phone, String phoneOther, String address, String detailAddress, String selectLabel, String userId
                        String name = mEtName.getText().toString().trim();
                        String sex = "女士";
                        if (mRbMan.isChecked()) {
                            sex = "先生";
                        }
                        String phone = mEtPhone.getText().toString().trim();
                        String phoneOther = mEtPhoneOther.getText().toString().trim();
                        String address = mEtReceiptAddress.getText().toString().trim();
                        String detailAddress = mEtDetailAddress.getText().toString().trim();
                        String selectLabel = mTvLabel.getText().toString().trim();
                        boolean ok = mAddressDao.insertAddress(new RecepitAddress(9999, name, sex, phone, phoneOther, address, detailAddress, selectLabel, "36"));
                        if (ok) {
                            finish();
                        } else {
                            Toast.makeText(this, "请仔细检查您填写的资料", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    String[] mTitles = new String[]{"无", "学校", "公司", "家"};
    String[] mBgColors = new String[]{"#FFE6AA6A", "#FF32E9DA", "#FF9432E9", "#FF51E932"};

    private void alertSelectLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择地址标签");
        builder.setItems(mTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTvLabel.setTextColor(Color.BLACK);
                mTvLabel.setText(mTitles[which]);
                //背景颜色
                mTvLabel.setBackgroundColor(Color.parseColor(mBgColors[which]));
            }
        });

        builder.show();
    }


    public boolean checkReceiptAddressInfo() {
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show();
            return false;
        }
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        String receiptAddress = mEtReceiptAddress.getText().toString().trim();
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请填写收获地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        String address = mEtDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isMobileNO(String phone) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return phone.matches(telRegex);
    }

    @OnClick(R.id.btn_location_address)
    public void onClick() {
    }
}
