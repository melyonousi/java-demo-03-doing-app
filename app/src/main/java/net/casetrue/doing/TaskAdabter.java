package net.casetrue.doing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdabter extends BaseAdapter {
    Context context;
    ArrayList<Task> tasks;

    public TaskAdabter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    static class ViewHolder {
        TextView txtTask;
        TextView txtDate;
        TextView txtTime;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
            viewHolder.txtTask = (TextView) convertView.findViewById(R.id.s_describe);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.s_date);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.s_time);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.txtTask.setText(tasks.get(position).getTask());
        viewHolder.txtDate.setText(tasks.get(position).getDate());
        viewHolder.txtTime.setText(tasks.get(position).getTime());
        convertView.setTag(viewHolder);

        return convertView;
    }
}
