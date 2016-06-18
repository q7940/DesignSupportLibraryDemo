package com.lqc.designsupportlibrarydemo.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.lqc.designsupportlibrarydemo.app.R;
import com.lqc.designsupportlibrarydemo.app.data.bean.Todos;
import com.lqc.designsupportlibrarydemo.app.data.db.TodoDao;

import java.util.List;

/**
 * Created by albert on 16-6-18.
 */
public class MemoRecyclerViewAdapter extends RecyclerView.Adapter<MemoRecyclerViewAdapter.MemoViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> mCon;

    /**
     * 自定义每个item点击与长恩点击的监听器接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int positon);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MemoRecyclerViewAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public MemoRecyclerViewAdapter(Context context, List<String> mCon){
        this(context);
        this.mCon = mCon;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemoViewHolder holder, int position) {
        final TextView view = (TextView)holder.mView.findViewById(R.id.memo_item_tv);
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
        view.setText(mCon.get(position));
//        view.setLayoutParams(lp);

        if (mOnItemClickListener!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    Toast.makeText(context,
                            "点击事件",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeData(holder.getLayoutPosition());

                    //删除数据库
                    //TODO:内容相同的时候，删除一个，会把数据库所有内容相同的删掉，但是界面只删除1个引起的bugs
                    TodoDao todoDao = new TodoDao(context);
                    List<Todos> Dtodo = todoDao.get(Todos.CON_FIELD_NAME, mCon.get(holder.getLayoutPosition()));
                    for (Todos todo:Dtodo){
                        todoDao.delete(todo);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCon.size();
    }

    /**
     * 动态增加数据
     * @param position 添加的位置
     * @param con 内容
     */
    public void addMemo(int position, String con){
        mCon.add(position, con);
        notifyItemInserted(position);
    }

    /**
     * 动态删除数据
     * @param
     */
    public void removeData(int position){
        mCon.remove(position);
        notifyItemRemoved(position);
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        public MemoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}
