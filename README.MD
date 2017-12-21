# Instant Search on Android
A sample project showing how to implement instant searching on Android.  
This sample uses Algolia but that can be changed to any other data storage by modifying
[SearchHelper.kt](/app/src/main/java/com/marknkamau/realtimesearch/SearchHelper.kt)

## How to
You will **need** to add `ALGOLIA_APPLICATION_ID` and `API_KEY` to gradle.properties. You can find these in the algolia dashboard.  

If you want to use the data set I used:
* Import the data in [sample-data.json](sample-data.json) to your Algolia account.

If you want to use your own data set:
* Import you data set to algolia
* Modify [Models.kt](/app/src/main/java/com/marknkamau/realtimesearch/models.kt), 
[DataAdapter.kt](/app/src/main/java/com/marknkamau/realtimesearch/DataAdapter.kt) and 
[item_data.xml](/app/src/main/res/layout/item_data.xml) to fit you data



