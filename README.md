<h1>Demo of the app :</h1>
https://youtu.be/Dyv7E_sddAc


<h1>Goal:</h1>
<p> Display this list of items to the user based on the following requirements: </p>

<li>Display all the items grouped by "listId"</li>
<li>Sort the results first by "listId" then by "name" when displaying.</li>
<li>Filter out any items where "name" is blank or null.</li>


<h1>Functional requirements:</h1>
<li>Fetching data from S3</li>
<li>Filter out items with null or empty names</li>
<li>Group items by listId</li>
<li>Sort names</li>

<h1>Non functional requirements:(These are my assumptions):</h1>
<li>Efficient querying</li>
<li>Data availability (except for the first time, there should be data available to the user even in face of network unavailability ).</li>

<h1>Component/System design:</h1>

<p>Assumptions:
The data in S3 would change over time as would with any real world database.</p>

<h2>Approach 1:</h2>

![Approach 1](https://i.ibb.co/CBpR1Zc/approach1.png)

The app calls for the data from the S3, downloads it, and displays as per requirement (grouping, sorting)

<h3>Pros:</h3>
<li>Easy to implement</li>


<h3>Cons:</h3>
<li>Slow and expensive :</li>
<li>The data is fetched from S3 every time.</li>


<h2>Approach 2:</h2>

![Approach 2](https://i.ibb.co/ydSZwTb/approach2.png)

<b>Components:</b>
<li>Front-End</li>
<li>In-Memory cache</li>

<b>Working:</b>
<p>The front-end checks in-memory cache and returns the data, if the cache is not empty. Else the cache populates the data from S3 , and returns it to the front-end.</p>

<h3>Pros:</h3>
<li>Comparatively complex to the previous approach (owing to cache), but still easy to implement.</li>
<li>Efficient data access. Data is retrieved from cache instead of fetching from S3. </li>

<h3>Cons:</h3>
<li>In-Memory cache will be lost when the app is killed. We lose the efficiency edge every time the app is restarted.</li>
<li>Any update to data in S3 would only be reflected when the app is reopened, as the cache has no knowledge of updates taking place in S3 (stale caching).</li>


<h2>Approach 3: (Implemented Approach)</h2>

![Approach 3](https://i.ibb.co/h7fJsW3/approach3.png)

<b>Components:</b>
<li>ViewModel : Holds the data needed for the UI</li>
<li>Repository : Used to manage multiple data sources</li>
<li>DAO: Abstraction over database layer.</li>
<li>Database: Local persistent database within the client to expedite data retrieval instead of making multiple network calls.</li>



<b>Working: </b>
<p>The data from S3 is stored and persisted locally in a Room Database. This database has an observer called LiveData which informs about changes in the data.
The data stored in the local database is passed on from DAO to the ViewModel in the chain. </p>


<h3>Pros:</h3>
<li>Single responsibility principle followed.</li>
<li>Persistent caching via local database.</li>
<li>Downloads data from S3 only when required and not for each query.</li>

<h3>Cons:</h3>
<li>Complex implementation</li>



<h1>Flow diagram: </h1>

![Flow Diagram](https://i.ibb.co/vcrrmfV/flow-diagram.png)



Model View requests data from Repository
Repository checks if there is an update in S3 metadata(using a HTTP request) by comparing the last modified timestamp in S3 file metadata vs the one stored in the metadata table..
If no recent file update: 
Repository returns data from ItemDao (database).
If S3 file has been updated: 
Repository calls ItemDao for refreshing its data from S3 , returns the fresh data and also updates the last modified timestamp in the metadata table.




<h1>Database design:</h1>


Entity for Items:

![Item Entity](https://i.ibb.co/5KpRZHQ/item-entity.png)

Entity for metadata:

![Metadata entity](https://i.ibb.co/VJ3RxGR/metadataentity.png)


@items_table:

id  | listId | name
286 | 1      | “item_286”


@metadata_table:

| timestamp |
|1631259886 |




<h1>API and Object oriented Design:</h1>

<li> List<Integer> getListIds() </li>
  
<li> List<String> getItemNames(int listId) </li>
  
<h2>Components and classes</h2>

| UI               	| Main Activity  Name Activity      	|
|------------------	|-----------------------------------	|
| ViewModel        	| ItemViewModel                     	|
| Repository       	| ItemRepository                    	|
| DAO              	| ItemsDao MetadataDao S3PayloadDao 	|
| Models(Entities) 	| Item Metadata                     	|
| Adapters         	| ListIdAdapter ItemViewHolder      	|
| Services         	| CheckNetwork                      	|
| Database         	| ItemDatabase                      	|


<h2>Notes:</h2>
<li>ModelViews know nothing about UI, they simply hold state</li>
<li>DAO are wrapped by LiveData objects for observation of change in data</li>
<li>Repository decides if the data is to be used from S3 or Room(local database)</li>
<li>Separate Threads used in the follow activities, to avoid blocking the main(UI) thread:</li>
<p>Note: Usage can be found  in ItemRepository class</p>


<h1>UI design:</h1>
<h2>Requirements:</h2>
<li>A screen showing Parent list (listIds list)</li>
<li>A screen showing Child list (names list)</li>
<li>Handle network disconnectivity</li>
<li>Handle empty/null data</li>


<h1>Screen shots :</h1>

![](https://i.ibb.co/7NSX8Ch/fetch-parent-list.jpg)

![](https://i.ibb.co/T1cFCCL/fetch-child-list.jpg)

![No network connection toast](https://i.ibb.co/F7DTGfP/fetch-no-network.jpg)

![Empty data](https://i.ibb.co/BKSc3cc/fetch-screen-shot-no-data.jpg)
