# UUIDFetcher
New UUIDFetcher ([since Mojang removed username history API](https://help.minecraft.net/hc/en-us/articles/8969841895693))

# How does it work?
It works just like the old UUIDFetchers (#getUUID, #getName), just with JSON.
At calling any of the methods - if not already in the cache - will
connect to [this address](https://api.ashcon.app/mojang/v2/user/KeineSecrets), in order to get the
username and the uuid. After that, if not already, it will automatically add it to the uuid and name cache
in order to get it faster next time.

# What do you need?
<li>[The GSON library](https://github.com/google/gson)</li>
<li>[The JSON library](https://github.com/stleary/JSON-java)</li>

# You may use it for any purpose and you may improve the code
# by forking, editing, push requesting
