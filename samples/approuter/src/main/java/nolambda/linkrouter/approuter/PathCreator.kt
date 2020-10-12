package nolambda.linkrouter.approuter

import nolambda.linkrouter.RoutePath

object Protocol : RoutePath(
    "app://",
    "http://",
    "https://"
)

val AppPath = Protocol.wrap(RoutePath(
    "m.bukatoko.com",
    "www.bktk.com",
    "bktk.link"
))

/* --------------------------------------------------- */
/* > For sample purpose */
/* --------------------------------------------------- */

object AppLink : RoutePath(
    "app://",
    "app://bukatoko.com",
    "app://bktk.com"
)

object WebLink : RoutePath(
    "http://bukatoko.com",
    "https://bukatoko.com"
)

val AllLink = AppLink + WebLink