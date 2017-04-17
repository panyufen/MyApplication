package com.example.pan.mydemo.activity.anim;

import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.utils.SvgPathParser;
import com.example.pan.mydemo.view.SvgPathImproveView;

public class SvgPathAnimActivity extends BaseActivity implements SvgPathImproveView.StatusListener {

    private Toolbar mToolbar;

    private TextView textView;
    private SvgPathImproveView svgPathView;
    Path path = null;
    SvgPathParser svgPathParser;

    private String playing = "正在绘制";
    private String end = "绘制完成";

    /**
     * 图片转svg 软件 用Vector Magic
     * http://liuyouth.github.io/utils/svg2android/index.html  查看Path的网站
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_path_anim);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.iron_man2);
        textView = (TextView) findViewById(R.id.text_view);
        svgPathView = (SvgPathImproveView) findViewById(R.id.svg_path_view);

        svgPathParser = new SvgPathParser();

        try {
            // iron_man,iron_man2,worldmap2
            path = svgPathParser.parsePath(getString(R.string.iron_man2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        svgPathView.setPath(path);
        svgPathView.setStatusListener(this);


    }

    public void doRestart(View v) {
        svgPathView.setPath(path);
        svgPathView.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("iron_man");
        menu.add("iron_man2");
        menu.add("world_map");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            // iron_man,iron_man2,worldmap2,shuangzixing
            switch ((String) item.getTitle()) {
                case "iron_man":
                    path = svgPathParser.parsePath(getString(R.string.iron_man));
                    mToolbar.setNavigationIcon(R.mipmap.iron_man);
                    break;
                case "iron_man2":
                    path = svgPathParser.parsePath(getString(R.string.iron_man2));
                    mToolbar.setNavigationIcon(R.mipmap.iron_man2);
                    break;
                case "world_map":
                    path = svgPathParser.parsePath(getString(R.string.world_map));
                    mToolbar.setNavigationIcon(R.mipmap.world_map);
                    break;

            }

            mToolbar.setTitle(item.getTitle());
            svgPathView.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void svgPathStart() {
        textView.setText(playing);
    }

    @Override
    public void svgPathChange(float progress) {
        textView.setText(playing + " \n" + String.format("%.2f", progress) + "%");
    }

    @Override
    public void svgPathEnd() {
        textView.setText(end);
    }
}
