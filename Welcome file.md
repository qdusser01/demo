---


---

<h1 id="what-is-squash-tm-">What is Squash TM ?</h1>
<p>Squash TM is one of the best open source management test tools for test repository management.</p>
<p>It has been designed for sharing test projects between stakeholders.</p>
<p>Squash TM offers the following features:</p>
<ul>
<li>Requirements management</li>
<li>Test cases management</li>
<li>Link requirements and test cases</li>
<li>Campaigns management</li>
<li>Report management: custom reports (charts, dashboards) for requirements</li>
<li>Custom fields</li>
<li>Connection with Bugtrackers: Jira, Mantis, Bugzilla</li>
<li>Connection with Agile project management tool: Jira (plugin Xsquash)</li>
<li>Dynamic dashboards for each workspace</li>
<li>Gherkin editor</li>
<li>Automation workspace for developers and testers</li>
</ul>
<p>Squash TM also offers specific features designed for collaborative work:</p>
<ul>
<li>Multi- and inter- project possibilities</li>
<li>“Full web” tool, which does not need to be set up on the client workstation</li>
<li>Intuitive and ergonomic design, RIA (Rich Internet Application) technology</li>
<li>Light administration &amp; user management</li>
</ul>
<p>All documentation is available here: <a href="https://sites.google.com/a/henix.fr/wiki-squash-tm/">https://sites.google.com/a/henix.fr/wiki-squash-tm/</a></p>
<h1 id="supported-tags-and-dockerfile-link">Supported tags and Dockerfile link</h1>
<p>The Dockerfile builds from “openjdk:8-jre-alpine” see <a href="https://hub.docker.com/_/openjdk">https://hub.docker.com/_/openjdk</a></p>
<p>The container taged latest will always refer to the last stable version of Squash TM. It means that to each release of Squash TM corresponds a docker image, starting from the version 1.19.1. (refering to Squash TM Docker image 1.19.1).</p>
<p><a href="https://github.com/">1.19.2, latest</a> (Dockerfile)</p>
<p><a href="https://github.com/">1.19.1</a> (Dockerfile)</p>
<h1 id="quick-start">Quick Start</h1>
<p>Run the Squash-TM image with an embedded h2 database (for demo purpose only !)</p>
<pre><code>docker run --name='squash-tm' -it -p 8090:8080 squashtest/squash-tm
</code></pre>
<p><em>NOTE</em>: To run the in daemon mode use the -d option :</p>
<pre><code>docker run --name='squash-tm' -d -it -p 8090:8080 squashtest/squash-tm
</code></pre>
<p>Please allow a few minutes for the applicaton to start, especially if populating the database for the first time. If you want to make sur that everything went fine, watch the log:</p>
<pre><code>docker exec -it squash-tm sh

tail -f squash-tm/logs/squash-tm.log
</code></pre>
<p>Go to <a href="http://localhost:8090/squash">http://localhost:8090/squash</a> or point to the IP of your docker host.</p>
<p>The default username and password are:</p>
<ul>
<li>username: <strong>admin</strong></li>
<li>password: <strong>admin</strong></li>
</ul>
<h1 id="configuration">Configuration</h1>
<h2 id="backing-up-data-with-persistent-volumes">Backing up data with persistent volumes</h2>
<p>As you may already know, in Docker you would generally keep data in volumes.</p>
<p>So, in order to use Squash TM image properly, it is highly recommended to set up an external database (MariaDB or PostgreSQL).</p>
<p>The following sections show how to deploy Squash TM using an external PostgreSQL DB container or an external MariaDB container. Exemples of yml file also show how to deploy this solution using docker-compose.</p>
<p>Each of these configurations povide the creation of a persistant volume for data.</p>
<pre><code>/var/lib/postgresql/data     # Data location using PostgreSQL
/var/lib/mysql               # Data location using MariaDB
</code></pre>
<p>Moreover, if your purpose is to use squash TM image in production, you’ll probably want to persist the following location in a volume in order to keep traces of logs.</p>
<pre><code>/opt/squash-tm/logs          # Log directory
</code></pre>
<p>For more info check Docker docs section on <a href="https://docs.docker.com/engine/tutorials/dockervolumes/">Managing data in containers</a></p>
<h2 id="deployment-using-postgresql">Deployment using PostgreSQL</h2>
<p>The database is created by the database container and automatically populated by the application container on first run.</p>
<p>All data from the database will be saved within the local volume named ‘squash-tm-db-pg’. So the db container (called ‘squash-tm-pg’) can be stop and restart with no risk of losing them.</p>
<pre><code>docker run -it -d --name='squash-tm-pg' \
-e POSTGRES_USER=squashtm \
-e POSTGRES_PASSWORD=MustB3Ch4ng3d \
-e POSTGRES_DB=squashtm \
-v squash-tm-db-pg:/var/lib/postgresql/data \
postgres:9.6.12 \

sleep 10

docker run -it -d --name=squash-tm \
--link squash-tm-pg:postgres \
-v squash-tm-logs:/opt/squash-tm/logs -v squash-tm-plugins:/opt/squash-tm/plugins \
-p 8090:8080 \
squashtest/squash-tm
</code></pre>
<p>Wait 3-4 minutes the time for Squash-TM to initialize. then log in to <a href="http://localhost:8090/squash-">http://localhost:8090/squash</a>  (admin  /  admin)</p>
<h2 id="deployment-using-mariadb">Deployment using MariaDB</h2>
<p>Database is created by the database container and automatically populated by the application container on first run.</p>
<p>All data from the database will be saved within the local volume named ‘squash-tm-db-mdb’. So the db container (called ‘squash-tm-mdb’) can be stop and restart with no risk of losing them.</p>
<pre><code>docker run -it -d --name='squash-tm-mdb' \
-e MYSQL_ROOT_PASSWORD=MustB3Ch4ng3d \
-e MYSQL_USER=squashtm \
-e MYSQL_PASSWORD=MustB3Ch4ng3d \
-e MYSQL_DATABASE=squashtm \
-v squash-tm-db-mdb:/var/lib/mysql \
mariadb:10.2.22-bionic --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci \

sleep 10

docker run -it -d --name=squash-tm \
--link squash-tm-mdb:mysql \
-v squash-tm-logs:/opt/squash-tm/logs -v squash-tm-plugins:/opt/squash-tm/plugins \
-p 8090:8080 \
squashtest/squash-tm
</code></pre>
<p>Wait about 10 minutes, the time for Squash-TM to initialize (yes, mysql initialisation is a bit longer than postres…). then log in to <a href="http://localhost:8090/squash-tm">http://localhost:8090/squash</a>  (admin  /  admin)</p>
<h2 id="docker-compose">Docker-Compose</h2>
<h3 id="docker-compose.yml-file"><code>docker-compose.yml file</code></h3>
<p>The following example of a docker-compose.yml link squash-tm to a MariaDB database. The environment variables should be set in a .env file (saved in the same repositority as the docker-compose.yml)</p>
<pre><code>version: '3.7'
services:
  squash-tm-md:
    image: mariadb:10.2.22-bionic
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_DATABASE}
    volumes:
      - "/var/lib/mysql-db:/var/lib/mysql"

  squash-tm:
    image: squashtest/squash-tm
    depends_on:
      - squash-tm-md
    environment:
      MYSQL_ENV_MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_ENV_MYSQL_USER: ${DB_USER}
      MYSQL_ENV_MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_ENV_MYSQL_DATABASE: ${DB_DATABASE}
    ports:
      - 8090:8080/tcp
    links:
      - squash-tm-md:mysql
    volumes:
      - squash-tm-logs:/opt/squash-tm/logs
      - squash-tm-plugins:/opt/squash-tm/plugins

volumes:
  squash-tm-logs:
  squash-tm-plugins:
</code></pre>
<h3 id="env-file"><code>.env file</code></h3>
<p>Here is an example of a .env file :</p>
<pre><code>DB_ROOT_PASSWORD=MustB3Ch4ng3d
DB_USER=squashtm
DB_PASSWORD=MustB3Ch4ng3d
DB_DATABASE=squashtm
</code></pre>
<h2 id="run-a-docker-compose">Run a docker-compose</h2>
<ol>
<li>Copy the <strong>docker-compose.yml</strong> that correspond to your need.</li>
</ol>
<p>You’ll find several docker-compose repository on our git :<br>
<em>Squash-tm deployment using MariaDB database : Link 1</em><br>
<em>Squash-tm deployment using Postgres database : Link 2</em><br>
<em>Squash-tm deployment using Postgres database and Reverse-Proxy : Link3</em><br>
<em>Squash-tm deployment using MariaDB database and Reverse-Proxy : Link 4</em></p>
<ol start="2">
<li>
<p>Don’t forget to create a <strong>.env</strong> file (or set the value of environment variables directly in the docker-compose.yml file).</p>
</li>
<li>
<p>In the docker-compose.yml directory, run docker-compose up or docker-compose up -d for daemon mode.</p>
</li>
<li>
<p>Log in to <a href="http://localhost:8090/squash">http://localhost:8090/squash</a></p>
</li>
</ol>
<p>For more information about docker-compose her is the <a href="https://docs.docker.com/compose/">documentation</a></p>
<h2 id="environment-variables">Environment variables</h2>
<p>As we are using images of existing DB container, we also use their environment variables.</p>
<p>These variables are used by Squash TM image script to connect to the database.</p>
<p>Here are some info about them and links leading to their documentation.</p>
<h3 id="postgres-image-environment-variables">Postgres image environment variables</h3>
<h4 id="postgres_password"><code>POSTGRES_PASSWORD</code></h4>
<p>This environment variable is recommended for you to use the PostgreSQL image. This environment variable sets the superuser password for PostgreSQL. The default superuser is defined by the <code>POSTGRES_USER</code> environment variable.</p>
<h4 id="postgres_user"><code>POSTGRES_USER</code></h4>
<p>This optional environment variable is used in conjunction with <code>POSTGRES_PASSWORD</code> to set a user and its password. This variable will create the specified user with superuser power and a database with the same name. If it is not specified, then the default user of <code>postgres</code> will be used.</p>
<h4 id="postgres_db"><code>POSTGRES_DB</code></h4>
<p>This optional environment variable can be used to define a different name for the default database that is created when the image is first started. If it is not specified, then the value of <code>POSTGRES_USER</code> will be used.</p>
<p>For further information and optional environment variables, please check out the <a href="https://hub.docker.com/_/postgres">Posgres image documentation</a></p>
<h3 id="mariadb-image-environment-variables">MariaDB image environment variables</h3>
<h4 id="mysql_root_password"><code>MYSQL_ROOT_PASSWORD</code></h4>
<p>This variable is mandatory and specifies the password that will be set for the MariaDB <code>root</code> superuser account.</p>
<h4 id="mysql_database"><code>MYSQL_DATABASE</code></h4>
<p>This variable is optional and allows you to specify the name of a database to be created on image startup. If a user/password was supplied then that user will be granted superuser access (<a href="http://dev.mysql.com/doc/en/adding-users.html">corresponding to <code>GRANT ALL</code></a>) to this database.</p>
<h4 id="mysql_user-mysql_password"><code>MYSQL_USER</code>, <code>MYSQL_PASSWORD</code></h4>
<p>These variables are optional, used in conjunction to create a new user and to set that user’s password. This user will be granted superuser permissions (see above) for the database specified by the <code>MYSQL_DATABASE</code> variable. Both variables are required for a user to be created.</p>
<p>Do note that there is no need to use this mechanism to create the root superuser, that user gets created by default with the password specified by the <code>MYSQL_ROOT_PASSWORD</code> variable.</p>
<p>For further information and optional environment variables, please check out the <a href="https://hub.docker.com/_/mariadb">MariaDB image documentation</a></p>
<h2 id="using-squash-tm-container-with-a-reverse-proxy">Using Squash TM container with a reverse proxy</h2>
<p>Two examples of docker-compose.yml deploying Squash TM behind a reverse proxy are available on our git :</p>
<p><em>Squash-tm deployment using Postgres database and Reverse-Proxy</em><br>
<em>Squash-tm deployment using MariaDB database and Reverse-Proxy</em></p>
<p>These solutions use a <a href="https://hub.docker.com/r/jwilder/nginx-proxy">docker image from jwilder based on nginx-proxy</a>.</p>
<p>Here is an example of Squash TM deployed behind a reverse-proxy using Postres database:</p>
<pre><code>version: '3.7'
services:
  squash-tm-pg:
    container_name: squash-tm-pg
    environment:
      POSTGRES_DB: ${DB_DATABASE}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_USER: ${DB_USER}
    image: postgres:9.6.12
    volumes:
      - /var/lib/db-postgresql:/var/lib/postgresql/data
    networks:
      - db-network

  squash-tm:
    depends_on:
      - squash-tm-pg
    environment:
      POSTGRES_ENV_POSTGRES_USER: ${DB_USER}
      POSTGRES_ENV_POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_ENV_POSTGRES_DB: ${DB_DATABASE}
      VIRTUAL_HOST: mysquash.example.com
    ports:
      - 8090:8080/tcp
    image: squashtest/squash-tm
    links:
      - squash-tm-pg:postgres
    volumes:
      - squash-tm-logs:/opt/squash-tm/logs
      - squash-tm-plugins:/opt/squash-tm/plugins
    networks:
      - nginx-proxy
      - db-network

  nginx-proxy:
    container_name: nginx
    image: jwilder/nginx-proxy
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - /var/run/docker.sock:/tmp/docker.sock:ro
    networks:
      - nginx-proxy

volumes:
  squash-tm-logs:
  squash-tm-plugins:

networks:
  nginx-proxy:
  db-network:

</code></pre>
<p><strong>References</strong></p>
<ul>
<li><a href="http://www.squashtest.org">http://www.squashtest.org</a></li>
<li><a href="https://github.com/Logicify/docker-squash-tm">https://github.com/Logicify/docker-squash-tm</a></li>
<li><a href="https://bitbucket.org/nx/squashtest-tm/wiki/WarDeploymentGuide">https://bitbucket.org/nx/squashtest-tm/wiki/WarDeploymentGuide</a></li>
<li><a href="https://confluence.atlassian.com/bitbucketserver/securing-bitbucket-server-behind-haproxy-using-ssl-779303273.html">https://confluence.atlassian.com/bitbucketserver/securing-bitbucket-server-behind-haproxy-using-ssl-779303273.html</a></li>
</ul>

