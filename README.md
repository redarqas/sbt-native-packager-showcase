play + sbt native packager + ansible
====================================

Targeted os (debian wheezy)

### Usage :

```
cd sbt-native-packager-showcase/ansible

ansible-galaxy install --role-file requirements.yml --roles-path roles

```

Usual mode : should start

```
ansible-playbook  -v -i local/inventory local.yml
```

Abnormal mode (emulate bad ansible provision) : should fail at startup

```
ansible-playbook  -v -i local/inventory local.yml --extra-vars "app_env=invalid"
```

### Wanted scenario

install app => ansible provision => start app

### Let do it with sbt native packager :


#### V 1.1.0 :

#### Scenario 1, Provision first : (tag - v1.1.0_scn1)

native packager starts the app automatically, then we changed our wanted scenario to : 

ansible provision => install app (start auto)

The risky thing with this is to have ansible provision overridden by app install defaults. 

#### V 1.1.1 :

#### Scenario 1, Provision first : (tag - v1.1.0_scn1)

On install we got the following error on wheezy : 

`Unable to install package: No debian archive, missing data.tar.{bz2,xz,lzma,gz,uncompressed,}`

[To solve this we added](http://www.scala-sbt.org/sbt-native-packager/formats/debian.html) `debianNativeBuildOptions in Debian := Seq("-Zgzip", "-z3")`  to ou build.sbt

Then wae obtained the following error : 
 
`/etc/init.d/showcase: line 56: syntax error near unexpected token }`

because of an unclosed if block.

V 1.1.1 is buggy and we can not use it even if the provision first scenario. 

#### V 1.2.x :

#### Scenario 1, Provision first : (tag - v1.2.0-M3_scn1)

Without `daemonStdoutLogFile` install is working 

With `daemonStdoutLogFile` the app is not started :

```
/etc/init.d/showcase start
[ ok ] Starting: showcase.
```
The start command is ok, however the app is not started. 

The error is in the `/etc/init.d/showcase` script : 
 
```
start-stop-daemon --chdir /var/novapost/showcase --chuid nova --pidfile /var/run/showcase/play.pid --startas /var/novapost/showcase/bin/showcase --start -- ' >> /var/log/showcase/boot.log 2>&1'

Bad root server path: /var/novapost/showcase/ >> /var/log/showcase/boot.log 2>&1
```

#### Scenario 2, Install first : (tag - v1.2.0-M3_scn2)

In order to run this scenario, we have to add the ability to disable auto start from sbt native packager. 


### Remarks 

#### play.pid extra management : 

Sometimes, for some reasons `paly.pid` remains after a failed app install. This prevents the next install from working right. 

To avoid this behavior we added the following ansible task :

```
if test -f /var/run/showcase/play.pid; then test -d /proc/$(</var/run/showcase/play.pid) || rm -rf /var/run/showcase; fi
```

It consists on the following : if `play.pid` is present and the app is not running, then remove `play.pid` 
