Backend Built through docker using this command:

```wsl
docker run --name repl-bank-db \
  -v repl-bank-postgres-data:/var/lib/postgresql \
  -e POSTGRES_PASSWORD=rootroot \
  -e POSTGRES_DB=repl-bank \
  -p 5432:5432 \
  -d postgres
```
