#!/bin/sh
# Deploy in local containers:
# ./run.sh
#
# Deploy a specific playbook on a specific env:
# INVENTORY=staging-eu/inventory PLAYBOOK=staging-eu.yml ./run.sh
set -eux

if [ -z "${VIRTUAL_ENV-}" ]; then
    Please activate a virtualenv
    exit 1
fi

# Force update of shared roles
rm -rf roles/peopledoc.*
ansible-galaxy install --role-file requirements.yml --roles-path roles

[ $USER = jenkins ] && [ $PLAYBOOK != "local.yml" ] && user="--user jenkins-back" || user=""

ansible-playbook -v --inventory ${INVENTORY-local/inventory} ${PLAYBOOK-local.yml} $user
