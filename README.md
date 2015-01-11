API-Challenge-CODE2040
======================

For me, the most challenging aspect was probably figuring out how to setup a connection properly and send JSONObjects. I had
never done anything like it in any language before so there was I had to do a whole bunch of reading beforehand. Eventually, I
figured it out and through it all I learned a ton. 

Stage One - I took a basic approach and just used java libraries to reverse the string for me.

Stage Two - Rather than compare the string to each item in the array I opted to create a sorted array first and then perform 
a binary search on the array. Depending on the size of the array this strategy seemeed to be much more cost-effective.

Stage Three - I ended up taking a brute force approach and just looped through the small array comparing the prefixes and removing them from a JSONArray.

Stage Four - This stage took the longest because I was unfamiliar with the ISO 8601 format. I ran into a problem with the 
updated time library for Java 8 since it does not support milliseconds, only nanoseconds and the timestamp included milliseconds. Either way, I saw that the nano/millisecond issue would not affect the resulting timestamp and so I did some string manipulation in order to create a LocalDateTime Object and add the number of seconds to that.

Overall I worked with quite a few new libraries which seem quite useful. Figuring out how to interact using HTTP was also interesting. 
