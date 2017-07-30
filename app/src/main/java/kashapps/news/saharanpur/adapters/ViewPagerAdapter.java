package kashapps.news.saharanpur.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedContent;

/**
 * Created by Wasim on 11-06-2015.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<FeedContent> feedContents;

    private PagerItemClickListener pagerItemClickListener;

    public ViewPagerAdapter(Context mContext, List<FeedContent> feedContents, PagerItemClickListener pagerItemClickListener) {
        this.mContext = mContext;
        this.feedContents = feedContents;
        this.pagerItemClickListener = pagerItemClickListener;
    }

    @Override
    public int getCount() {
        return feedContents.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        FeedContent feedContent = feedContents.get(position);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        TextView title = (TextView)itemView.findViewById(R.id.title_pager_item);
        Picasso.with(mContext).load(feedContent.getImage()).into(imageView);
        title.setText(feedContent.getTitle());
        container.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerItemClickListener.onPagerItemClick(view, position);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

    public interface PagerItemClickListener {
        void onPagerItemClick(View view, int position);
    }
}
