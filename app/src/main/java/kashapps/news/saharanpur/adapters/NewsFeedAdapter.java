package kashapps.news.saharanpur.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;
import com.squareup.picasso.Picasso;

import kashapps.news.saharanpur.R;
import kashapps.news.saharanpur.api.responses.FeedContent;
import kashapps.news.saharanpur.api.responses.NewsFeedResponse;

/**
 * Created by ankur on 29/7/17.
 */

public class NewsFeedAdapter extends BaseSwipeAdapter<BaseSwipeAdapter.BaseSwipeableViewHolder> {
    private static final int TYPE_PAGER = 0;
    private static final int TYPE_FEED_LIST = 1;

    public static final int PAGER_ITEMS_COUNT = 4;

    private int currentPagerItem = 0;

    private Context context;
    private NewsFeedResponse feedResponse;

    private FeedItemClickListener feedItemClickListener;
    private ViewPagerAdapter.PagerItemClickListener pagerItemClickListener;

    public NewsFeedAdapter(Context context, NewsFeedResponse feedResponse, FeedItemClickListener feedItemClickListener, ViewPagerAdapter.PagerItemClickListener pagerItemClickListener) {
        this.context = context;
        this.feedResponse = feedResponse;
        this.feedItemClickListener = feedItemClickListener;
        this.pagerItemClickListener = pagerItemClickListener;
    }

    public void setNewsFeedResponse(NewsFeedResponse feedResponse) {
        this.feedResponse = feedResponse;
    }

    @Override
    public BaseSwipeableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PAGER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_pager_view, parent, false);
            return new FeedPagerViewHolder(view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_feed_view, parent, false);
            return new FeedItemViewHolder(view, feedItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(BaseSwipeableViewHolder holder, int position) {

        if (position == 0) {
            FeedPagerViewHolder pagerViewHolder = (FeedPagerViewHolder)holder;
            ViewPagerAdapter mAdapter = new ViewPagerAdapter(context, feedResponse.getContent().subList(0, PAGER_ITEMS_COUNT), pagerItemClickListener);
            pagerViewHolder.intro_images.setAdapter(mAdapter);
            pagerViewHolder.intro_images.setCurrentItem(currentPagerItem);
            pagerViewHolder.setUiPageViewController(mAdapter);
        }
        else {
            FeedContent feedContent = feedResponse.getContent().get(position + PAGER_ITEMS_COUNT - 1);
            FeedItemViewHolder itemViewHolder = (FeedItemViewHolder)holder;
            Picasso.with(context).load(feedContent.getImage()).into(itemViewHolder.imageView);
            itemViewHolder.title.setText(feedContent.getTitle());
            itemViewHolder.summary.setText(feedContent.getSummary());
            itemViewHolder.feedDate.setText(feedContent.getDate());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_PAGER;
        else
            return TYPE_FEED_LIST;
    }

    @Override
    public int getItemCount() {
        return feedResponse.getContent().size() - PAGER_ITEMS_COUNT + 1;
    }

    private class FeedPagerViewHolder extends BaseSwipeableViewHolder implements ViewPager.OnPageChangeListener   {

        private ImageButton btnNext, btnFinish;
        private ViewPager intro_images;
        private LinearLayout pager_indicator;
        private int dotsCount;
        private ImageView[] dots;

        public FeedPagerViewHolder(View view) {
            super(view);

            intro_images = (ViewPager) view.findViewById(R.id.pager_introduction);
            btnNext = (ImageButton) view.findViewById(R.id.btn_next);
            btnFinish = (ImageButton) view.findViewById(R.id.btn_finish);

            pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
            intro_images.setOnPageChangeListener(this);
        }

        public void setUiPageViewController(ViewPagerAdapter mAdapter) {
            pager_indicator.removeAllViews();
            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(context);
                dots[i].setImageDrawable(context.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }

            dots[currentPagerItem].setImageDrawable(context.getResources().getDrawable(R.drawable.selecteditem_dot));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPagerItem = position;
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(context.getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }

            dots[position].setImageDrawable(context.getResources().getDrawable(R.drawable.selecteditem_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class FeedItemViewHolder extends BaseSwipeableViewHolder {
        public ImageView imageView;
        public TextView title;
        public TextView summary;
        public TextView feedDate;

        public FeedItemViewHolder(View view, final FeedItemClickListener feedItemClickListener) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.feed_image);
            title = (TextView) view.findViewById(R.id.feed_title);
            summary = (TextView)view.findViewById(R.id.feed_summary);
            feedDate = (TextView)view.findViewById(R.id.feed_date);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedItemClickListener.onFeedItemClick(view);
                }
            });
        }
    }

    public interface FeedItemClickListener {
        void onFeedItemClick(View view);
    }
}
