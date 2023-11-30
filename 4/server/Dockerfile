FROM php:7.4-apache

RUN apt-get update && apt-get install -y default-mysql-client
RUN docker-php-ext-install mysqli && docker-php-ext-enable mysqli

RUN a2enmod rewrite

CMD ["apache2-foreground"]