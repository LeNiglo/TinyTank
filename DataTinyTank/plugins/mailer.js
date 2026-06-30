// Lightweight mailer: renders a Pug template from emails/ and sends it via
// nodemailer. Replaces the abandoned express-mailer (which pulled in an EOL
// nodemailer@0.7 + aws-sdk@2 chain with known vulnerabilities).

var path = require('path');
var pug = require('pug');
var nodemailer = require('nodemailer');

var FROM = 'TinyTank <no-reply@tinytank.lefrantguillaume.com>';

var transporter = nodemailer.createTransport({
  host: process.env.SMTP_HOST,
  port: process.env.SMTP_PORT ? Number(process.env.SMTP_PORT) : 587,
  auth: process.env.SMTP_USER
    ? { user: process.env.SMTP_USER, pass: process.env.SMTP_PASSWORD }
    : undefined
});

// send(templateName, locals, cb)
//   locals must include `to` and `subject`; the rest are passed to the template.
exports.send = function (template, locals, cb) {
  cb = cb || function () {};
  try {
    var html = pug.renderFile(path.join(__dirname, '..', 'emails', template + '.pug'), locals);
    transporter.sendMail({
      from: FROM,
      to: locals.to,
      subject: locals.subject,
      html: html
    }, cb);
  } catch (err) {
    cb(err);
  }
};
