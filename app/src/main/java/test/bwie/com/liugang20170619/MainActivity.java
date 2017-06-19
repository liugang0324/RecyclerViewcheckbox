package test.bwie.com.liugang20170619;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
/**
 * @ Description:主业务操作类  RecyclerView嵌套checkbox
 * @ Date:2017/6/19
 * @ Author:刘刚
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button check_all;
    private Button check_reveser;
    private RecyclerView recycler;
    private LinearLayout activity_main;
    private MyAdapter adapter;
    SparseArray<Boolean> checkStates;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDate();
    }

    private void initView() {
        check_all = (Button) findViewById(R.id.check_all);   //根据ID初始化控价
        check_reveser = (Button) findViewById(R.id.check_reveser);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        activity_main = (LinearLayout) findViewById(R.id.activity_main);

        check_all.setOnClickListener(this);   //设置checkbox监听事件
        check_reveser.setOnClickListener(this);

        adapter = new MyAdapter();
        recycler.setAdapter(adapter);   //设置适配器
        recycler.setLayoutManager(new LinearLayoutManager(this)); //添加布局管理器
        recycler.setItemAnimator(new DefaultItemAnimator());  //添加默认动画
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL)); //添加默认分割线

        //条目的点击事件和长按事件
        adapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(MainActivity.this, "这是条目"+(position+1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                dialog.setMessage("这是条目"+(position+1));
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_all:
             selectAll();
                break;
            case R.id.check_reveser:
                   reverse();
                break;
        }
    }
    //    反选
    private void reverse() {
        for (int i = 0; i < checkStates.size(); i++) {
            if (checkStates.valueAt(i)) {
                checkStates.setValueAt(i, false);
            } else {
                checkStates.setValueAt(i, true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //    全选
    private void selectAll() {
        for (int i = 0; i < checkStates.size(); i++) {
            checkStates.setValueAt(i, true);
        }
        adapter.notifyDataSetChanged();
    }

    //    初始化
    private void initDate() {
        checkStates = new SparseArray<>();
        for (int i = 0; i < 100; i++) {
            checkStates.put(i, false);
        }
    }
  //为Recyclerview设置适配器
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener, View.OnLongClickListener {
        //接口实例
        public RecyclerViewOnItemClickListener onItemClickListener;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item,parent,false);
            MyViewHolder  holder = new MyViewHolder(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText((position+1)+"");
            holder.checkBox.setText(checkStates.keyAt(position)+"");
            //设置Tag
            holder.root.setTag(position);
//            用户点击checkbox行为会需要增加监听来改变checkStates对应项的状态
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("check",position+":"+isChecked);
                        checkStates.put(position,isChecked);

                }
            });

            holder.checkBox.setChecked( checkStates.get(position));
        }

        @Override
        public int getItemCount() {
            return checkStates.size();
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
            }
        }
        //长按事件
        @Override
        public boolean onLongClick(View v) {
            //不管显示隐藏，清空状态
            return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, (Integer) v.getTag());
        }

        //设置点击事件
        public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

    }
    //接口回调设置点击事件
    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);

        //长按事件
        boolean onItemLongClickListener(View view, int position);
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public TextView tv;
        private View root;
        public MyViewHolder(View root) {
            super(root);
            this.root = root;
            checkBox = (CheckBox)root.findViewById(R.id.cb);
            tv = (TextView) root.findViewById(R.id.tv);
        }
    }
}
