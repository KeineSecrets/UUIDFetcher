# UUIDFetcher
New UUIDFetcher ([since Mojang removed username history API](https://help.minecraft.net/hc/en-us/articles/8969841895693))

# How does it work?
It works just like the old UUIDFetchers (#getUUID, #getName), just with JSON.<br>
At calling any of the methods - if not already in the cache - will<br>
connect to [this address](https://api.ashcon.app/mojang/v2/user/KeineSecrets), in order to get the<br>
username and the uuid. After that, if not already, it will automatically add it to the uuid and name cache<br>
in order to get it faster next time.

# What do you need?
**»** [The GSON library](https://github.com/google/gson)<br>
**»** [The JSON library](https://github.com/stleary/JSON-java)

## You may use it for any purpose and you may improve the code<br>by forking, editing, push requesting
