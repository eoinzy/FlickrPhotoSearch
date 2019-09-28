# FlickrPhotoSearch
This is a sample app that queries the Flickr API and searches for a photo with a given tag.

The app uses the MVP architecture for the user interface, and the data later is abstracted into its own repository.
This app uses RetroFit to download the JSON data, and Picasso to load the images.

An API key is required to query Flickr.
Set you API key (and secret) in you local gradle.properties file. On *nix machines, this is ~/.gradle/gradle.properties, in the form:
<p>
FLICKR_API_KEY=1234567890ABCDEF <br />
FLICKR_API_SECRET=123123123ABC
</p>
