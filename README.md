# Stock-trading
Server-Client connection using ports.

## Functionality table
| Function  |  C# |  Java |
|---|---|---|
| Client establishes a connection with the server  | ✓  | ✓  |
| Client is assigned a unique ID when joining the market  |  ✓ |  ✓ |
|  Client displays up-to-date information about the market state | ✓  |  ✓ |
|  Client allows passing the stock to another player | ✓  | ✓  |
|  Server manages multiple client connections | ✓  | ✓  |
| Server accepts new connections while traders are exchanging stock among themselves  | ✓  |  ✓ |
|  Server correctly handles clients leaving the market | ✓  |  ✓ |
| Client is compatible with the server in the other language  | ✓  |  ✓ |
|  Client GUI |   | ✓  |
| Server GUI  |   |  ✓ |
| A new server is created by clients when the old is closed  |   | ✓  |
| New server transition is seamless to clients |   | ✓  |

## Protocol

Both server and client run on port 8888.

Accounts - when a client requests accounts the server will respond with multiple lines. The first line consisting of the number of accounts currently in the market. With all subsequent lines consisting of the id of a user in the market.

Send – this request must be followed by an integer. Upon receiving a send request the server will first check if the user making the request has the stock and then check if the account the stock being sent to exists. If either of these checks fail then an error message will be returned. If not, then the stock will be transferred and the server will return with “success”.

Stock – when this request is made the server will return on a single line the id of the account with the stock.

Id- when this request is made the server will return on a single line the id of the clients account.

