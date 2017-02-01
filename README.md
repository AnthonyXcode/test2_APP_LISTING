# test2_APP_LISTING

![](http://i.giphy.com/26xBAckT1okLXrjjy.gif)

1. Add permission at manifests to access internet
   android:name="android.permission.INTERNET"

2. Add picasso to help loading image and volley to get API data at build gradle in app level

   compile 'com.squareup.picasso:picasso:2.5.2'
   compile 'com.mcxiaoke.volley:library:1.0.19'
   
3. Use VolleyGet to make reusable string request.
4. ImageUtility to make a smooth experience when scrol downward.
5. Use RecyclerView to make more comstomied thing.

![](http://i.giphy.com/26gssW7z21eqZ1Uzu.gif)

6. The fellowing method help to do research action:

   private void searchApp(final String appName){
        ArrayList<ListingItem> preparedGrossingItems = new ArrayList<>();
        for (ListingItem item: grossingItems){
            if (item.getName().toLowerCase().contains(appName.toLowerCase())){
                preparedGrossingItems.add(item);
            }
        }
        adapter.updateGrossingForSearch(preparedGrossingItems);

        ArrayList<ListingItem> prepared100Items = new ArrayList<>();
        for (ListingItem item:top100Items){
            if (item.getName().toLowerCase().contains(appName.toLowerCase())){
                prepared100Items.add(item);
            }
        }
        adapter.updateTop00ForSearch(prepared100Items);

    }
