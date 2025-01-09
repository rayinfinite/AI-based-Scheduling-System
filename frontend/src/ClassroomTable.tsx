import { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { ClassroomType, deleteClassroom, getClassroom, saveClassroom } from "./api";

const formSchema = z.object({
  name: z.string().min(1, "Please input the Classroom Name!"),
  size: z.number().min(1, "Please input the Classroom Capacity!"),
  software: z.string().optional(),
});

const ClassroomTable = forwardRef<{ add: () => void }, object>((_, ref) => {
  const [data, setData] = useState<ClassroomType[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState<Partial<ClassroomType> | null>(null);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      size: 0,
      software: "",
    },
  });

  useImperativeHandle(ref, () => ({
    add,
  }));

  const edit = (record: Partial<ClassroomType>) => {
    setEditingRecord(record);
    setIsModalOpen(true);
  };

  const save = async (values: z.infer<typeof formSchema>) => {
    if (values.software === "") {
      delete values.software;
    }
    if (editingRecord && editingRecord.id) {
      await saveClassroom({ ...values, id: editingRecord.id });
    } else {
      await saveClassroom(values as ClassroomType);
    }
    await fetch();
    setIsModalOpen(false);
    setEditingRecord(null);
    form.reset();
  };

  const add = () => {
    setEditingRecord(null);
    setIsModalOpen(true);
  };

  const handleDelete = async (key: number) => {
    await deleteClassroom(key);
    await fetch();
  };

  const fetch = async () => {
    const data = await getClassroom();
    setData(data);
  };

  useEffect(() => {
    fetch();
  }, []);

  useEffect(() => {
    if (editingRecord) {
      form.setValue("name", editingRecord.name || "");
      form.setValue("size", Number(editingRecord.size) || 0);
      form.setValue("software", editingRecord.software || "");
    } else {
      form.reset();
    }
  }, [editingRecord, form]);

  return (
    <>
      <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editingRecord ? "Edit classroom" : "Create a new classroom"}</DialogTitle>
          </DialogHeader>
          <DialogDescription />
          <Form {...form}>
            <form onSubmit={form.handleSubmit(save)} className="space-y-4">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Classroom</FormLabel>
                    <FormControl>
                      <Input {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="size"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Capacity</FormLabel>
                    <FormControl>
                      <Input type="number" {...field} onChange={(e) => field.onChange(parseInt(e.target.value))} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="software"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Software</FormLabel>
                    <FormControl>
                      <Input {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <div className="flex justify-end space-x-2">
                <Button variant="outline" onClick={() => setIsModalOpen(false)}>
                  Cancel
                </Button>
                <Button type="submit">{editingRecord ? "Save" : "Create"}</Button>
              </div>
            </form>
          </Form>
        </DialogContent>
      </Dialog>

      <div className="border rounded-md">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Classroom</TableHead>
              <TableHead>Capacity</TableHead>
              <TableHead>Software</TableHead>
              <TableHead className="w-[100px]">Operation</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {data.map((record) => (
              <TableRow key={record.id}>
                <TableCell>{record.name}</TableCell>
                <TableCell>{record.size}</TableCell>
                <TableCell>{record.software || "N/A"}</TableCell>
                <TableCell>
                  <div className="flex space-x-2">
                    <Button variant="link" size="sm" onClick={() => edit(record)}>
                      Edit
                    </Button>
                    <Button
                      variant="link"
                      size="sm"
                      onClick={() => {
                        if (window.confirm(`Confirm to delete ${record.name}?`)) {
                          handleDelete(record.id);
                        }
                      }}
                    >
                      Delete
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </>
  );
});

export default ClassroomTable;
