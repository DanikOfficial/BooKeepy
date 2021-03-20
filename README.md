# BooKeepy API
This is an API that you can use to store information about books that you Own or Wish!

Concise Documentation: <https://bookeepyapi.netlify.app/>

API Url: <https://bookeepyapi.herokuapp.com/>

## Version: 1.0

**Contact information:**  
Pete João Chiboleca  
pete9450@gmail.com  

#### PUT 
### /api/v1/image/{bookId}/cover/{imageId}

##### Summary

Sets an image as cover.

##### Description

Sets an image as book cover!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| bookId | path | id of the book. | Yes | long |
| imageId | path | id of the image that will be set as cover. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successfully removed the book. |
| 404 | Either the book or image doesn't exist. |


#### GET
### /api/v1/book/{id}

##### Summary

Fetch using an Id.

##### Description

Fetch a book using its Id.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the book to be fetched. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Fetched the book of the specified id and it's images. |
| 404 | Could not find the book. |

#### PUT
### /api/v1/book/{id}

##### Summary

Updates a book.

##### Description

Updates a book from the Database!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the book that will be updated. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successfully updated the book. |
| 400 | Constraints violated. |
| 404 | The specified Book doesn't exist. |
| 409 | Failed to add new book because the specified ISBN already exists. |

#### DELETE
### /api/v1/book/{id}

##### Summary

Removes a book.

##### Description

Removes a book from the Database!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the book that will be removed. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successfully removed the book. |
| 404 | Book doesn't exist. |


#### POST
### /api/v1/images/{id}

##### Summary

Adds images.

##### Description

Adds images associated with a book!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the book. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Successfully added images. |
| 400 | Constraints violated. |
| 404 | The specified Book doesn't exist. |


#### POST
### /api/v1/image/{id}

##### Summary

Adds an image.

##### Description

Adds an image associated with a book!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the book. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Successfully added the image. |
| 400 | Constraints violated. |
| 404 | The specified Book doesn't exist. |

#### DELETE
### /api/v1/image/{id}

##### Summary

Removes an image.

##### Description

Removes an image from the Database!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id of the image that will be removed. | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successfully removed the image. |
| 404 | The specified Image doesn't exist. |

#### GET 
### /api/v1/books

##### Summary

Fetches all the books.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Fetched all Books from the Database. |

#### POST 
### /api/v1/books

##### Summary

Adds a new book

##### Description

Adds a new book to the Database

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | Successfully added a new book. |
| 400 | Constraints violated. |
| 409 | Failed to add new book because the specified ISBN already exists. |

#### GET 
### /api/v1/books/{status}

##### Summary

Fetches all books based on the specified book status.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| status | path | The status to be filtered | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Fetched all books based on book status. |
| 400 | Invalid book status. |


#### DELETE
### /api/v1/images/{ids}

##### Summary

Removes images.

##### Description

Removes images from the Database!

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| ids | path | array of id of the images that will be removed. | Yes | [ integer ] |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | Successfully removed the images. |

### Models

#### GenericErrors

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| message | string |  | No |
| timeStamp | dateTime |  | No |
| status | string |  | No |

#### Image

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| image_url | string |  | Yes |
| image_location | string |  | No |
| isCover | boolean |  | No |

#### BookRequestObject

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| title | string |  | Yes |
| status | string |  | Yes |
| author | string |  | Yes |
| edition | string |  | Yes |
| isbn | string |  | No |

#### Book

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| title | string |  | Yes |
| author | string |  | Yes |
| edition | string |  | Yes |
| status | string |  | Yes |
| isbn | string |  | No |
| images | [ object ] |  | No |

#### ValidationErrors

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| field | string |  | No |
| errorMessage | string |  | No |

#### ImageRequestObject

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| image_url | string |  | No |
| isCover | boolean |  | No |
| image_location | string |  | No |

#### BookView

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long |  | No |
| title | string |  | No |
| status | string |  | No |
| author | string |  | No |
| image_url | string |  | No |
