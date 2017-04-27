'use strict';

// Configuration
import configuration from './settings';

// Express/HTTP setup
import express from 'express';
import http from 'http';
const app = express();
const server = http.Server(app);

import morgan from 'morgan';
import path from 'path';

// Add logging middleware
app.use(morgan('dev'));

// Add static file-serving middleware
app.use(express.static(path.join(__dirname, 'public')));

// Start HTTP server
server.listen(configuration.port, function (err) {
  if (err) {
    throw err;
  }
  else {
    console.log('Running on http://localhost:' + configuration.port);
  }
});
