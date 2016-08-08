package com.rodvar.mvppet.presentation;

import java.util.Arrays;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rodvar.mvppet.R;
import com.rodvar.mvppet.data.network.ServerAPI;
import com.rodvar.mvppet.presentation.adapter.BaseViewHolder;
import com.rodvar.mvppet.presentation.adapter.ClassViewHolderType;
import com.rodvar.mvppet.presentation.adapter.IBaseProgressAdapter;
import com.rodvar.mvppet.presentation.adapter.SimpleListAdapter;
import com.rodvar.mvppet.presentation.adapter.SimpleViewHolder;

import butterknife.Bind;
import nucleus.factory.RequiresPresenter;
import rx.functions.Action1;

/**
 * Created by rodrigo on 23/07/16.
 * Shows the lists of events
 */
@RequiresPresenter(MainPresenter.class)
public class MainFragment extends BaseFragment<MainPresenter, ServerAPI.Item> {

    @Bind(R.id.events_view)
    RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    TextView emptyView;
    private GridLayoutManager layoutManager;
    private SimpleListAdapter<ServerAPI.Item> adapter;

    /**
     * @return new MainFragment instance
     */
    public static MainFragment newInstance() {
        return new MainFragment();
    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int pastVisiblesItems, visibleItemCount, totalItemCount;
            if (dy > 0) { //scroll down
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if (isReadyToLoadMoreData()) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        setReadyToLoadMoreData(false);
                        Log.v(getClass().getSimpleName(), "Request more data");
                        requestData();
                    }
                }
            }
        }
    };

    @Override
    protected void doOnCreateView(View rootView) {
        // decorate recycle recyclerView?
        this.layoutManager = new GridLayoutManager(this.getActivity(), 2);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.addOnScrollListener(this.scrollListener);
        this.adapter = new SimpleListAdapter<>(R.layout.recycler_view_progress,
                new ClassViewHolderType<>(ServerAPI.Item.class, R.layout.item, new ClassViewHolderType.ViewHolderFactory<ServerAPI.Item>() {
                    @Override
                    public BaseViewHolder<ServerAPI.Item> call(final View view) {
                        return new SimpleViewHolder<>(view, new Action1<ServerAPI.Item>() {
                            @Override
                            public void call(ServerAPI.Item item) {
                                Toast.makeText(getActivity(), "click " + item, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }));
        this.recyclerView.setAdapter(this.adapter);
    }

    @Override
    public void onDestroy() {
        this.recyclerView.removeOnScrollListener(this.scrollListener);
        super.onDestroy();
    }

    @Override
    protected void doOnRequestSucceded(ServerAPI.Item[] items) {
        this.adapter.add(Arrays.asList(items));
    }

    @Override
    protected void doOnRequestFailed(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected IBaseProgressAdapter getMainAdapter() {
        return this.adapter;
    }

    @Override
    protected View getEmptyView() {
        return this.emptyView;
    }

    @Override
    protected View getMainRecyclerView() {
        return this.recyclerView;
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_main;
    }
}
