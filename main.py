import lightbulb
import urllib.request, json

bot = lightbulb.Bot(token="...", prefix="!")


@bot.command()
async def comp(ctx):
    resp = calculate_response(ctx.content[5:])
    await ctx.respond(resp)


def calculate_response(clanTag):
    if clanTag.startswith('#'):
        clanTag = clanTag[1:8]
    if not clanTag:
        clanTag = "9JUVCV0L"
    endpoint = "https://fwastats.com/Clan/" + clanTag + "/Members.json"
    with urllib.request.urlopen(endpoint) as url:
        data = json.loads(url.read().decode())
    return calculate_weights(data)


def calculate_weights(data):
    wtbyth = {}
    for m in data:
        if m["townHall"] in wtbyth:
            wtbyth[m["townHall"]] = wtbyth[m["townHall"]] + 1
        else:
            wtbyth[m["townHall"]] = 1
    return wtbyth


bot.run()
