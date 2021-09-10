<h1>Demo of the app :</h1>

<h1>Goal:</h1>
Display a list of items in a nest list form
MVP
<h1>Functional requirements:</h1>
Fetching data from S3
Filter out items with null or empty names
Group items by listId
Sort names

<h1>Non functional requirements:(These are my assumptions):</h1>
Efficient querying
Data availability (except for the first time, there should be data available to the user even in face of network unavailability ).

<h1>Component/System design:</h1>

Assumptions:
The data in S3 would change over time as would with any real world database.

Approach 1:



The app calls for the data from the S3, downloads it, and displays as per requirement (grouping, sorting)

Pros:
Easy to implement


Cons:
Slow and expensive :
The data is fetched from S3 every time.


Approach 2:



Components:
Front-End
In-Memory cache
S3 

Working:
The front-end checks in-memory cache and returns the data, if the cache is not empty. Else the cache populates the data from S3 , and returns it to the front-end.

Pros:
Comparatively complex to the previous approach (owing to cache), but still easy to implement.
Efficient data access. Data is retrieved from cache instead of fetching from S3. 

Cons:
In-Memory cache will be lost when the app is killed. We lose the efficiency edge every time the app is restarted.
Any update to data in S3 would only be reflected when the app is reopened, as the cache has no knowledge of updates taking place in S3 (stale caching).














Approach 3: (Implemented Approach)


Components:
ViewModel : Holds the data needed for the UI
Repository : Used to manage multiple data sources
DAO: Abstraction over database layer.
Database: Local persistent database within the client to expedite data retrieval instead of making multiple network calls.



Working: 
The data from S3 is stored and persisted locally in a Room Database. This database has an observer called LiveData which informs about changes in the data.
The data stored in the local database is passed on from DAO to the ViewModel in the chain. 


Pros:
Single responsibility principle followed.
Persistent caching via local database.
Downloads data from S3 only when required and not for each query.

Cons:
Complex implementation



<h1>Flow diagram: </h1>





Model View requests data from Repository
Repository checks if there is an update in S3 metadata(using a HTTP request) by comparing the last modified timestamp in S3 file metadata vs the one stored in the metadata table..
If no recent file update: 
Repository returns data from ItemDao (database).
If S3 file has been updated: 
Repository calls ItemDao for refreshing its data from S3 , returns the fresh data and also updates the last modified timestamp in the metadata table.




<h1>Database design:</h1>


Entity for Items:



Entity for metadata:



@items_table:
id
listId
name
286
1
“item_286”



@metadata_table:

timestamp
1631259886




<h1>API and Object oriented Design:</h1>

Components and classes





UI
MainActivity
NameActivity
ViewModel
ItemViewModel
Repository
ItemRepository
DAO
ItemsDao
MetadataDao
S3PayloadDao		
Models(Entities)
Item
Metadata
Adapters
ListIdAdapter
ItemViewHolder	
Services
CheckNetwork				
Database
ItemDatabase



Principles followed:
Single responsibility principle

Notes:
ModelViews know nothing about UI, they simply hold state
DAO are wrapped by LiveData objects for observation of change in data
Repository decides if the data is to be used from S3 or Room(local database)
Separate Threads used in the follow activities, to avoid blocking the main(UI) thread:
Data insertion
Data retrieval
Note: Usage can be found  in ItemRepository class

<h1>UI design:</h1>
Requirements:
A screen showing Parent list (listIds list)
A screen showing Child list (names list)
Handel network disconnectivity
Handle empty/null data
