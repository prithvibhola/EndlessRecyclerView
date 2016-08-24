# Endless RecyclerView with Linear, Grid and Staggered Layout

##Learning how to use RecyclerView with Linear, Grid and Staggered Layout in your project.

> Does your application uses RecyclerView to show images of different width and height. Showing the images with different aspect ratio could be difficult. Here is the simple way you can use to display the images of various aspect ratio in Recyclerview.

> Code also includes how to implement endless RecyclerView.

# Screenshots 

![ScreenShot](/screenshots/RecyclerView_Grid.png)
![ScreenShot](/screenshots/RecyclerView_Linear.png)
![ScreenShot](/screenshots/RecyclerView_Staggered.png)
# Setting up project

*Required for Linear and Staggered Layout*
## Create a DynamicHeightNetworkImageView.java file which extends ImageView and will be used in your XML.

```
public class DynamicHeightNetworkImageView extends ImageView {
    private float mAspectRatio = 1.5f;

    public DynamicHeightNetworkImageView(Context context) {
        super(context);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }
}

```
# Endless RecyclerView for LinearLayout

## 1. Add <DynamicHeightNetworkImageView/> tag in your XML

```
<recyclerview.prithvi.utils.DynamicHeightNetworkImageView
    android:id="@+id/ivImage"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:adjustViewBounds="true"/>
```

## 2. Create an object of LinearLayoutManager

```
LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
```

## 3. Create Variables

```
private int visibleItemCount, totalItemCount, firstVisibleItem;
private int previousTotal = 0, pageCount = 1, visibleThreshold = 4;
private boolean loading = true;
```

## 4. Setup endless RecyclerView

```
private void setUpRecyclerView(RecyclerView rv){
    rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
    rv.setLayoutManager(mLinearLayoutManager);
    rv.setItemAnimator(new DefaultItemAnimator());

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(RecyclerView rv, int dx, int dy) {
            super.onScrolled(rv, dx, dy);

            String url;

            visibleItemCount = mRecyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    pageCount++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)){
                url = URL + "&page=" + String.valueOf(pageCount);
                sendJSONRequest(url);
                loading = true;
            }
        }
    });
    rv.setAdapter(moviesAdapter);
}
```

## 5. Adjust the aspect ratio of the image in your Adapter.

```
Glide.with(context)
      .load(imageUrl)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .crossFade()
      .into(holder.imageUnsplash);
float aspectRatio = calculateAspectRatio(Integer.parseInt(currentImage.getWidth()), Integer.parseInt(currentImage.getHeight()));
holder.imageUnsplash.setAspectRatio(aspectRatio);
```
```
private float calculateAspectRatio(float width, float height){
        float aspectRatio = width / height;
        return aspectRatio;
}
```

# Endless RecyclerView for GridLayout

## 1. Add ImageView in your XML

```
<ImageView
    android:id="@+id/ivImage"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:adjustViewBounds="true"/>
```
> ImageView is used since layout_height is fixed in case of gridLayoutManager to show proper grids.
> If you want the aspect ratio to be maintained, please see the StaggeredLayout explained below.

## 2. Create an object of GridLayoutManager

```
GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
```

## 3. Create Variables

```
private int visibleItemCount, totalItemCount, firstVisibleItem;
private int previousTotal = 0, pageCount = 1, visibleThreshold = 4;
private boolean loading = true;
```

## 4. Setup endless RecyclerView

```
private void setUpRecyclerView(RecyclerView rv){
    rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
    rv.setLayoutManager(mGridLayoutManager);
    rv.setItemAnimator(new DefaultItemAnimator());

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
        @Override
        public void onScrolled(RecyclerView rv, int dx, int dy) {
            super.onScrolled(rv, dx, dy);

            String url;

            visibleItemCount = mRecyclerView.getChildCount();
            totalItemCount = mGridLayoutManager.getItemCount();
            firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    pageCount++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)){
                url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);
                sendJSONRequest(url);
                loading = true;
            }
        }
    });
    rv.setAdapter(moviesAdapter);
}
```
## 5. NO need to maintain aspect ration in GridLayout.

```
Glide.with(context)
      .load(imageUrl)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .crossFade()
      .into(holder.imageUnsplash);
```
>As the image size is fixed, their is not need to add the function for aspect ratio.

# Endless RecyclerView for StaggeredLayout

## 1. Add <DynamicHeightNetworkImageView/> in your XML

```
<recyclerview.prithvi.utils.DynamicHeightNetworkImageView
    android:id="@+id/ivImage"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:adjustViewBounds="true"/>
```

## 2. Create an object of StaggeredGridLayoutManager

```
StaggeredGridLayoutManager mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
```

## 3. Create Variables

```
private int visibleItemCount, totalItemCount, pastVisibleItems;
private int previousTotal = 0, pageCount = 1, visibleThreshold = 4;
private boolean loading = true;
```
> **Note:** firstVisibleItem variable is not required in StaggeredLayout rather an array of firstVisibleItem is needed inside function setUpRecyclerView.(See below)

## 4. Setup endless RecyclerView

```
private void setUpRecyclerView(RecyclerView rv){
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setLayoutManager(mStaggeredGridLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);

                String url;

                **int[] firstVisibleItem = null;**
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mStaggeredGridLayoutManager.getItemCount();
                **firstVisibleItem = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItem);**

                **if(firstVisibleItem != null && firstVisibleItem.length > 0){**
                    **pastVisibleItems = firstVisibleItem[0];**
                **}**

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + visibleThreshold)){
                    url = PHOTOS_URL + "&page=" + String.valueOf(pageCount);
                    sendJSONRequest(url);
                    loading = true;
                }
            }
        });
        rv.setAdapter(moviesAdapter);
    }
}
```

## 5. Adjust the aspect ratio of the image in your Adapter.

```
Glide.with(context)
      .load(imageUrl)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .crossFade()
      .into(holder.imageUnsplash);
float aspectRatio = calculateAspectRatio(Integer.parseInt(currentImage.getWidth()), Integer.parseInt(currentImage.getHeight()));
holder.imageUnsplash.setAspectRatio(aspectRatio);
```
```
private float calculateAspectRatio(float width, float height){
        float aspectRatio = width / height;
        return aspectRatio;
}
```

>#Code and Demo apk has been included in the repository.

