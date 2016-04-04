# wReport
A plugin for Sponge to report bad players and start a vote kick (under development).

#### Features

- [x] Report System
- [ ] GUI
- [ ] Vote kick
- [ ] SQL Support

#### Reasons

##### Players

- [x] Aggression
- [ ] Illegal Program
- [ ] Bug Abuse
- [ ] Flood
- [ ] Spam

##### Others

- [ ] Bug
- [ ] Others (like suggestions)

#### Action

- [x] Ban
- [ ] Kick
- [ ] Block Chat (all or specific channels if supported)

### Commands

```
/report [player] [reason] [description]
```

Permission: `wreport.player.report`

```
/wradmin show
```

Permission: `wreport.admin.reports.show`

```
/wradmin close [reportid] [action] "[description]"  [action parameters]
```

Permission: `wreport.admin.reports.close`

**Obs** If you are closing report via Terminal probably you will have to type like that:
```
/wradmin close [reportid] [action] \"[description]\"  [action parameters]
```

**Obs** If you miss any `action parameters` the plugin will show 'action' usage description.