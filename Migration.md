# Data Migration

Migration of existing data to the new platform.

## Getting the data out of MongoDB

```bash
brew install mongodb
mongoexport -h host -u username -p password -d database -c collection -o existing_data.json
```
