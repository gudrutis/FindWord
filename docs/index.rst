Introduction page
=======================================

This an example situp to test READ THE DOCS infrastructure.

https://github.com/jonasdaugalas/brilview/tree/master/docs

https://github.com/rtfd/readthedocs.org/blob/master/docs/index.rst



First steps
-----------

This is example layout to check it it is posible to mix .rst and .md files in one
documentation.

This is some text to see changes.

* **Refering to other document**:
  :doc:`document <dir1/text1>`

.. _user-docs:

.. toctree::
   :maxdepth: 3
   :caption: This is example of constructing document tree

   dir1/text1
   README
   Top_level

