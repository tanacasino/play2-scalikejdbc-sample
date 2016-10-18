#!/bin/bash

mysql -uroot -e "DROP DATABASE IF EXISTS play2_scalikejdbc_sample;"
mysql -uroot -e 'CREATE DATABASE play2_scalikejdbc_sample DEFAULT CHARACTER SET utf8mb4;'

mysql -uroot -e "DROP DATABASE IF EXISTS play2_scalikejdbc_sample_test;"
mysql -uroot -e 'CREATE DATABASE play2_scalikejdbc_sample_test DEFAULT CHARACTER SET utf8mb4;'

