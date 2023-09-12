FROM php:7.4-apache

RUN apt-get update && apt-get install -y default-mysql-client

COPY index.php /var/www/html
COPY style.css /var/www/html
COPY httpd.conf /etc/apache2/apache2.conf
COPY init.sql /docker-entrypoint-initdb.d/init.sql

RUN docker-php-ext-install mysqli && docker-php-ext-enable mysqli
RUN a2enmod rewrite

EXPOSE 8080

CMD ["apache2-foreground"]