# EasyListView-Android
Creating listview without making adapter

Add it in your root build.gradle at the end of repositories:
- Add the JitPack repository to your build file
```
allprojects {
     repositories {
    ...
    maven { url 'https://jitpack.io' }
     }
}
```

- Add the dependency
```
dependencies {
      compile 'com.github.yudhistiroagung:EasyListView-Android:v0.1.2'
}
```

## Usage
- Implement your data class with ListItem interface, like below :
```
// we are using Product.class as example
public class Product implements ListItem {

    String productName;
    String description;
    String imageUrl;

    public Product(String productName, String description, String imageUrl) {
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    
    public String getProductName(){
        return this.productName;
    }

    @Override
    public String getTitle() {
        return this.productName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }
}
```
- add EasyListView to your layout file
There are 3 types of list view : LIST_VIEW, GRID_VIEW, STAGGERED_GRID_VIEW
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.yudhistiroagung.easylistviewexample.MainActivity">

    <com.yudhistiroagung.easylistview.EasyListView
        android:id="@+id/list_view"
    app:easyListViewType="LIST_VIEW" //choose yout type here
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

</android.support.constraint.ConstraintLayout>
```
- Add data and click listener on your activity 
```
public class MainActivity extends AppCompatActivity {

    private EasyListView mListView;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.list_view); //get listview object
        mListView.setListItems(getMockProducts()); //set data to list

       //set click listener
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(int position, ListItem listItem) {
                Toast.makeText(MainActivity.this, "Clicked "+position, Toast.LENGTH_SHORT).show();
        
                //example if you want to access your class method
                if(listItem instanceof Product){
                    String prodName = ((Product) listItem).getProductName();
                }
            }
        });

        //
        mEasyListView.setOnScrollEndListener(new OnScrollEndListener() {
            @Override
            public void onScrollEnd() {
                if (!isLoading) {
                    Toast.makeText(MainActivity.this, "Loading ...", Toast.LENGTH_SHORT).show();
                    isLoading = !isLoading;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //fake network request
                            isLoading = !isLoading;
                            mEasyListView.addListItems(getMockProducts());
                        }
                    }, 3000);
                }
            }
        });
    }

    // example of dummy datas
    public List<ListItem> getMockProducts(){
        List<ListItem> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            res.add( new Product("Title "+i, "Description "+i, "http://dreamstop.com/wp-content/uploads/2013/11/Fruit-Dreams.jpg") );
        }
        return res;
    }

}
```

MIT License
Copyright (c) 2017 Yudhistiro Agung N

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.