package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by dejun.xie on 2016/7/25.
 */
public class MyLvAdapter extends BaseAdapter {

    public static final int TYPE_ONE=0;
    public static final int TYPE_TWO=1;
    private List<ItemData> list;
    private Context context;
    private ImageView contentImageView;
    private TextView contentTextView;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private LeftDragItemView leftDragItemView;
    private LeftWrapperItem leftWrapperItem;
    private int llDragWidth;

    public MyLvAdapter(Context context, List<ItemData> list) {
        this.context = context;
        this.list = list;
        initData();
    }

    private void initData() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//            Log.d("XDJ","convertView == null,position="+position);
            ViewHolder viewHolder = null;
            RightDragItemView itemView = null;
            if (getItemViewType(position) == TYPE_ONE) {
                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=layoutInflater.inflate(R.layout.item_type_1,null,false);
//                convertView = View.inflate(context, R.layout.item_type_1, null);
                RightDragItemView rightDragItemView = (RightDragItemView) convertView.findViewById(R.id.right_item_view);
                contentImageView = rightDragItemView.getIv();
                contentTextView = rightDragItemView.getTv();
//                convertView = rightDragItemView;

                //强制设置左边的布局宽度为屏幕宽
                ViewGroup.LayoutParams layoutParams=rightDragItemView.getLlContent().getLayoutParams();
                layoutParams.width=displayMetrics.widthPixels;

                //“标记已读”的判断
                if(list.get(position).getHasBeenRead()){
                    rightDragItemView.getTvTab().setText("标记未读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.gray));
                }else{
                    rightDragItemView.getTvTab().setText("标记已读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.black));
                }
                //设置删除的监听
                setOnRightDeleteListener(position, rightDragItemView);
                //设置置顶的监听
                setOnRightTopListener(position, rightDragItemView);
                //设置“标记已读”的监听
                setOnRightReadListener(rightDragItemView, position);

            } else if (getItemViewType(position) == TYPE_TWO) {
//                LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView=layoutInflater.inflate(R.layout.item_type_2,null);
                convertView = View.inflate(context, R.layout.item_type_2, null);
                leftDragItemView = (LeftDragItemView) convertView.findViewById(R.id.right_item_view);
                leftWrapperItem = (LeftWrapperItem) convertView.findViewById(R.id.wrapper_item);
                LinearLayout llDrag= (LinearLayout) convertView.findViewById(R.id.ll_drag);

                llDrag.measure(0, 0);
                llDragWidth = llDrag.getMeasuredWidth();
                leftDragItemView.setLlDragWidth(llDragWidth);

//                leftDragItemView.scrollBy(llDragWidth,0);
                convertView.scrollTo(llDragWidth, 0);
                contentImageView = (ImageView) convertView.findViewById(R.id.iv_right);
                contentTextView = (TextView) convertView.findViewById(R.id.tv_left);

                //设置删除的监听
                setOnLeftDeleteListener(position, leftDragItemView);
                //设置置顶的监听
                setOnLeftTopListener(position, leftDragItemView);
                //设置“标记已读”的监听
                setOnLeftReadListener(position, leftDragItemView);
            }

            viewHolder = new ViewHolder();
            viewHolder.setIv(contentImageView);
            viewHolder.setTv(contentTextView);
            convertView.setTag(viewHolder);
        } else {
//            Log.d("XDJ","convertView != null,position="+position);
            if (getItemViewType(position) == TYPE_ONE) {
                RightDragItemView rightDragItemView = (RightDragItemView) convertView.findViewById(R.id.right_item_view);
                rightDragItemView.backToHide();
                //“标记已读”的判断
//                Log.d("XDJ","list.get("+position+").getHasBeenRead()="+list.get(position).getHasBeenRead());
                if(list.get(position).getHasBeenRead()){
                    rightDragItemView.getTvTab().setText("标记未读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.gray));
                }else{
                    rightDragItemView.getTvTab().setText("标记已读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.black));
                }
                //重新设置“删除”的监听
                setOnRightDeleteListener(position, rightDragItemView);
                //重新设置“置顶”的监听
                setOnRightTopListener(position, rightDragItemView);
                //重新设置“标记已读”的监听
                setOnRightReadListener(rightDragItemView, position);
            }else if(getItemViewType(position) == TYPE_TWO){
//                Log.d("XDJ", "getItemViewType(position) == TYPE_TWO");
//                convertView.scrollTo(llDragWidth, 0);
                leftDragItemView = (LeftDragItemView) convertView.findViewById(R.id.right_item_view);

                //“标记已读”的判断
//                Log.d("XDJ","list.get("+position+").getHasBeenRead()="+list.get(position).getHasBeenRead());
                if(list.get(position).getHasBeenRead()){
                    leftDragItemView.getTvTab().setText("标记未读");
                    leftDragItemView.getTvContent().setTextColor(context.getResources().getColor(R.color.gray));
                }else{
                    leftDragItemView.getTvTab().setText("标记已读");
                    leftDragItemView.getTvContent().setTextColor(context.getResources().getColor(R.color.black));
                }

                //重新设置删除的监听
                setOnLeftDeleteListener(position, leftDragItemView);
                //重新设置置顶的监听
                setOnLeftTopListener(position, leftDragItemView);
                //重新设置“标记已读”的监听
                setOnLeftReadListener(position, leftDragItemView);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            contentImageView = viewHolder.getIv();
            contentTextView = viewHolder.getTv();
        }

        contentImageView.setImageResource(list.get(position).getDrawableId());
        contentTextView.setText(list.get(position).getFileName());

        return convertView;
    }

    private void setOnLeftTopListener(final int position, final LeftDragItemView leftDragItemView) {
        leftDragItemView.getTvTop().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnLeftTopListener() position=" + position);

                //已经是第一个了，则不需要再置顶了
                if(position==0){
                    leftDragItemView.backToHide();
                    return;
                }else{
                    ItemData removetem=list.remove(position);
                    list.add(0, removetem);
                    notifyDataSetChanged();
                    leftDragItemView.backToHide();
                }
            }
        });
    }

    private void setOnLeftReadListener(final int position, final LeftDragItemView leftDragItemView) {
        final TextView tvTab=leftDragItemView.getTvTab();
        tvTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnLeftReadListener()");

                //当点击“标记未读”时
                if (list.get(position).getHasBeenRead()) {
                    tvTab.setText("标记已读");
                    leftDragItemView.getTvContent().setTextColor(context.getResources().getColor(R.color.black));
                    list.get(position).setHasBeenRead(false);
                }
                //当点击“标记已读”时
                else {
                    tvTab.setText("标记未读");
                    leftDragItemView.getTvContent().setTextColor(context.getResources().getColor(R.color.gray));
                    list.get(position).setHasBeenRead(true);
                }

//                convertView.scrollTo(llDragWidth,0);
//                Log.d("XDJ","before layout "
//                +",leftDragItemView.getLeft()="+leftDragItemView.getLeft()
//                +",leftDragItemView.getRight()="+leftDragItemView.getRight());

                leftDragItemView.backToHide();
            }
        });
    }

    private void setOnLeftDeleteListener(final int position, final LeftDragItemView leftDragItemView) {
        leftDragItemView.getTvDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnRightDeleteListener() position=" + position);
                list.remove(position);
                leftDragItemView.backToHide();
                notifyDataSetChanged();
            }
        });
    }

    private void setOnRightReadListener(final RightDragItemView rightDragItemView, final int position) {
        final TextView tvTab=rightDragItemView.getTvTab();
        tvTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnRightReadListener()");

                //当点击“标记未读”时
                if(list.get(position).getHasBeenRead()){
                    tvTab.setText("标记已读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.black));
                    list.get(position).setHasBeenRead(false);
                }
                //当点击“标记已读”时
                else{
                    tvTab.setText("标记未读");
                    rightDragItemView.getTv().setTextColor(context.getResources().getColor(R.color.gray));
                    list.get(position).setHasBeenRead(true);
                }
                rightDragItemView.backToHide();
            }
        });
    }

    private void setOnRightTopListener(final int position, final RightDragItemView rightDragItemView) {
        rightDragItemView.getTvTop().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnRightTopListener() position=" + position);

                //已经是第一个了，则不需要再置顶了
                if(position==0){
                    rightDragItemView.backToHide();
                    return;
                }else{
                    ItemData removetem=list.remove(position);
                    list.add(0, removetem);
                    notifyDataSetChanged();
                    rightDragItemView.backToHide();
                }
            }
        });
    }

    private void setOnRightDeleteListener(final int position, final RightDragItemView rightDragItemView) {
//        Log.d("XDJ","setOnRightDeleteListener position="+position);
        rightDragItemView.getTvDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("XDJ", "setOnRightDeleteListener() position=" + position);
                list.remove(position);
                rightDragItemView.backToHide();
                notifyDataSetChanged();
            }
        });
    }

    //ListView的缓存类
    class ViewHolder {

        private ImageView contentImageView;
        private TextView contentTextView;
//        private TextView tvDelete;
//        private TextView tvTop;
//        private TextView tvTab;

//        public TextView getTvDelete() {
//            return tvDelete;
//        }
//
//        public void setTvDelete(TextView tvDelete) {
//            this.tvDelete = tvDelete;
//        }
//
//        public TextView getTvTop() {
//            return tvTop;
//        }
//
//        public void setTvTop(TextView tvTop) {
//            this.tvTop = tvTop;
//        }
//
//        public TextView getTvTab() {
//            return tvTab;
//        }
//
//        public void setTvTab(TextView tvTab) {
//            this.tvTab = tvTab;
//        }

        public ImageView getIv() {
            return contentImageView;
        }

        public void setIv(ImageView contentImageView) {
            this.contentImageView = contentImageView;
        }

        public TextView getTv() {
            return contentTextView;
        }

        public void setTv(TextView tv) {
            this.contentTextView = tv;
        }
    }

    private int contenWidth;
    public int getContentWidth(){
        return contenWidth;
    }
    public void setContentWidth(int contentWidth){
        this.contenWidth=contentWidth;
    }
}
