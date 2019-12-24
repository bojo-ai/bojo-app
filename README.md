# BOJO.AI [![CircleCI](https://circleci.com/gh/bojo-ai/bojo-app.svg?style=svg)](https://circleci.com/gh/bojo-ai/bojo-app)

Api & web archive for the dumbest things BoJo has ever said.

## Usage

```shell
# Retrieve a random quote
$ curl --request GET \
       --url 'https://www.bojo.ai/random/quote' \
       --header 'Accept: application/hal+json'
```

Example quote response:
```json
{
  "appeared_at": "2003-07-22T00:00:00.000Z",
  "created_at": "2019-12-23T21:13:29.040Z",
  "quote_id": "c0D_6QSvTdC8t95ALENRBg",
  "tags": [
    "Music",
    "Elvis"
  ],
  "updated_at": "2019-12-23T21:13:29.040Z",
  "value": "I have as much chance of becoming Prime Minister as of being decapitated by a frisbee or of finding Elvis.",
  "_embedded": {
    "sources": [
      {
        "created_at": "2019-12-23T21:13:28.478Z",
        "filename": null,
        "quote_source_id": "in8rU9GMRxeeweuLiqz9yg",
        "remarks": "The Big Book of Boris",
        "updated_at": "2019-12-23T21:13:28.478Z",
        "url": "https://www.dailymail.co.uk",
        "_links": {
          "self": {
            "href": "https://www.bojo.ai/quote-source/in8rU9GMRxeeweuLiqz9yg"
          }
        }
      }
    ],
    "authors": [
      {
        "author_id": "wVE8Y7BoRKCBkxs1JkqBvw",
        "bio": null,
        "created_at": "2019-12-23T21:13:28.068Z",
        "name": "Boris Johnson",
        "slug": "boris-johnson",
        "updated_at": "2019-12-23T21:13:28.068Z",
        "_links": {
          "self": {
            "href": "https://www.bojo.ai/author/wVE8Y7BoRKCBkxs1JkqBvw"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "https://www.bojo.ai/quote/c0D_6QSvTdC8t95ALENRBg"
    }
  }
}
```

The application supports HAL through JSON, a simple format for a consistent and
easy way to hyperlink between resources which improves discoverability. Watch
out for the `_links` property within the response body and follow the embedded
links.

For more examples check the [OpenApi documentation](https://www.bojo.ai/documentation)
and have a look into the [Postman collection](./postman/ai.bojo.postman_collection.json).

## License

This distribution is covered by the **GNU GENERAL PUBLIC LICENSE**, Version 3, 29 June 2007.

## Support & Contact

Having trouble with this repository? Check out the documentation at the repository's site or contact m@matchilling.com and we’ll help you sort it out.

Happy Coding

:v: