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

???

#### V 1.2.x :

???

